package de.uni_augsburg.bazi.as;

import com.google.common.collect.Sets;
import com.google.common.collect.Table;
import de.schummar.castable.Data;
import de.uni_augsburg.bazi.common.UserCanceledException;
import de.uni_augsburg.bazi.common.algorithm.Algorithm;
import de.uni_augsburg.bazi.common.algorithm.Options;
import de.uni_augsburg.bazi.common.algorithm.Uniqueness;
import de.uni_augsburg.bazi.common.util.CollectionHelper;
import de.uni_augsburg.bazi.divisor.DivisorAlgorithm;
import de.uni_augsburg.bazi.divisor.DivisorData;
import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Real;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static de.uni_augsburg.bazi.common.algorithm.VectorData.Party;

class ASAlgorithmImpl
{
	public static Map<Object, Real> calculate(
		Table<DivisorData, String, Party> table,
		Map<Object, Int> seats,
		DivisorUpdateFunction divisorUpdateFunction,
		Algorithm<? extends DivisorData> divisorAlgorithm,
		Options options
	)
	{
		return new ASAlgorithmImpl(divisorUpdateFunction, divisorAlgorithm, table, seats, options).calculate();
	}


	Table<DivisorData, String, Party> table;
	Map<?, ? extends Map<?, Party>> rows, cols, rowsAndCols;
	Map<Object, Int> seats;
	private final DivisorUpdateFunction divisorUpdateFunction;
	private final Algorithm<? extends DivisorData> divisorAlgorithm;
	private final Options options;

	ASAlgorithmImpl(DivisorUpdateFunction divisorUpdateFunction, Algorithm<? extends DivisorData> divisorAlgorithm, Table<DivisorData, String, Party> table, Map<Object, Int> seats, Options options)
	{
		this.divisorUpdateFunction = divisorUpdateFunction;
		this.divisorAlgorithm = divisorAlgorithm;
		this.table = table;
		this.seats = seats;
		this.options = options;
		rows = table.rowMap();
		cols = table.columnMap();

		Map<Object, Map<?, Party>> rowsAndCols = new HashMap<>();
		rowsAndCols.putAll(rows);
		rowsAndCols.putAll(cols);
		this.rowsAndCols = rowsAndCols;
	}


	private Map<Object, Real> calculate()
	{
		Map<Party, Real> votes = table.values().stream().collect(
			Collectors.toMap(Function.identity(), Party::votes)
		);
		Set<Object> allKeys = Sets.union(table.rowKeySet(), table.columnKeySet());
		Map<Object, Real> divisors = allKeys.stream()
			.collect(Collectors.toMap(Function.identity(), key -> BMath.ONE));

		while (true)
		{
			if (Thread.interrupted())
				throw new UserCanceledException();

			// update faults
			Map<Object, Int> faults = rows.entrySet().stream().collect(
				Collectors.toMap(
					Map.Entry::getKey,
					row -> row.getValue().values().stream()
						.map(Party::seats)
						.reduce(Int::add).orElse(BMath.ZERO)
						.sub(seats.get(row.getKey()))
				)
			);
			if (faults.values().stream().allMatch(BMath.ZERO::equals))
				break;

			// for each row (or column when transposed)
			rows.entrySet().parallelStream().forEach(
				row -> {

					// scale each votes with the respective column divisor
					row.getValue().forEach(
						(colKey, party) -> party.votes(party.votes().div(divisors.get(colKey)))
					);

					// calculate an apportionment => the row sum will be correct
					DivisorData divisorData = Data.create(DivisorData.class);
					divisorData.seats(seats.get(row.getKey()));
					divisorData.parties().setAllData(row.getValue().values());
					divisorAlgorithm.apply(divisorData, options);


					// apply the calculated seats to the table and reset votes
					CollectionHelper.forEachPair(
						row.getValue().values(), divisorData.parties(), (p, temp) -> {
							p.votes(votes.get(p));
							p.seats(temp.seats());
							p.uniqueness(temp.uniqueness());
						}
					);

					// set the calculated divisor as row divisor
					divisors.compute(
						row.getKey(),
						(key, value) -> divisorUpdateFunction.apply(divisorData.divisor(), faults.get(key))
					);
				}
			);

			// transer ties to minimize faults
			transfer();

			// flip rows / cols
			transpose();
		}

		return divisors;
	}


	private void transfer()
	{
		while (true)
		{
			Set<Object> Iminus = new HashSet<>(), Iplus = new HashSet<>();

			cols.forEach(
				(colKey, col) -> {
					Int sum = col.values().stream()
						.map(Party::seats)
						.reduce(Int::add).orElse(BMath.ZERO);
					int comp = sum.compareTo(seats.get(colKey));
					if (comp < 0) Iminus.add(colKey);
					if (comp > 0) Iplus.add(colKey);
				}
			);

			Set<Object> L = new HashSet<>(Iminus);
			Queue<Object> Q = new LinkedList<>(L);
			Map<Object, Object> pre = new HashMap<>();

			while (!Q.isEmpty())
			{
				Object u = Q.poll();
				Uniqueness uniqueness = cols.containsKey(u)
					? Uniqueness.CAN_BE_MORE
					: Uniqueness.CAN_BE_LESS;

				rowsAndCols.get(u).forEach(
					(key, party) -> {
						if (!L.contains(key)
							&& party.votes().compareTo(0) > 0
							&& party.uniqueness() == uniqueness)
						{
							Q.add(key);
							L.add(key);
							pre.put(key, u);
						}
					}
				);
			}

			List<Object> L_Iplus = new ArrayList<>(L);
			L_Iplus.retainAll(Iplus);

			if (L_Iplus.isEmpty()) return;

			Object colKey = L_Iplus.get(0);
			do
			{
				Object rowKey = pre.get(colKey);
				Party party = cols.get(colKey).get(rowKey);
				party.seats(party.seats().sub(1));
				party.uniqueness(Uniqueness.CAN_BE_MORE);

				colKey = pre.get(rowKey);
				party = cols.get(colKey).get(rowKey);
				party.seats(party.seats().add(1));
				party.uniqueness(Uniqueness.CAN_BE_LESS);
			} while (!Iminus.contains(colKey));
		}
	}


	private void transpose()
	{
		Map<?, ? extends Map<?, Party>> temp = rows;
		rows = cols;
		cols = temp;
	}
}
