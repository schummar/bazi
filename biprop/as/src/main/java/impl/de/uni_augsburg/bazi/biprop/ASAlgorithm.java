package de.uni_augsburg.bazi.biprop;

import com.google.common.collect.Sets;
import com.google.common.collect.Table;
import de.uni_augsburg.bazi.common.UserCanceledException;
import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Real;
import de.uni_augsburg.bazi.monoprop.DivisorMethod;
import de.uni_augsburg.bazi.monoprop.DivisorOutput;
import de.uni_augsburg.bazi.monoprop.MonopropInput;
import de.uni_augsburg.bazi.monoprop.Uniqueness;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

class ASAlgorithm
{
	public static BipropResult calculate(
		Table<BipropInput.District, String, BipropOutput.Party> table,
		Map<Object, Int> seats,
		DivisorUpdateFunction divisorUpdateFunction,
		DivisorMethod divisorMethod
	)
	{
		return new ASAlgorithm(divisorUpdateFunction, divisorMethod, table, seats).calculate();
	}


	Table<BipropInput.District, String, BipropOutput.Party> table;
	Map<?, ? extends Map<?, BipropOutput.Party>> rows, cols, rowsAndCols;
	Map<Object, Int> seats;
	private final DivisorUpdateFunction divisorUpdateFunction;
	private final DivisorMethod divisorMethod;

	ASAlgorithm(DivisorUpdateFunction divisorUpdateFunction, DivisorMethod divisorMethod, Table<BipropInput.District, String, BipropOutput.Party> table, Map<Object, Int> seats)
	{
		this.divisorUpdateFunction = divisorUpdateFunction;
		this.divisorMethod = divisorMethod;
		this.table = table;
		this.seats = seats;
		rows = table.rowMap();
		cols = table.columnMap();

		Map<Object, Map<?, BipropOutput.Party>> rowsAndCols = new HashMap<>();
		rowsAndCols.putAll(rows);
		rowsAndCols.putAll(cols);
		this.rowsAndCols = rowsAndCols;
	}


	private BipropResult calculate()
	{
		Set<Object> allKeys = Sets.union(table.rowKeySet(), table.columnKeySet());
		Function<Object, Object> ID = o -> o;
		Map<Object, Int> faults = allKeys.stream().collect(Collectors.toMap(ID, key -> BMath.ZERO));
		Map<Object, Real> divisors = allKeys.stream().collect(Collectors.toMap(ID, key -> BMath.ONE));
		Int faultCount = BMath.ONE;

		while (!faultCount.equals(0))
		{
			if (Thread.interrupted())
				throw new UserCanceledException();

			// scale votes
			table.cellSet().forEach(
				(cell) -> {
					Real scale = divisors.get(cell.getRowKey())
						.mul(divisors.get(cell.getColumnKey()));
					cell.getValue().votes = cell.getValue().votes.div(scale);
				}
			);

			// calculate one apportionment per row (or column)
			rows.entrySet().parallelStream().forEach(
				row -> {
					DivisorOutput output = divisorMethod.calculate(
						MonopropInput.create(
							seats.get(row.getKey()),
							row.getValue().values()
						)
					);

					divisorMethod.calculate(
						new MonopropInput()
						{
							@Override
							public Int seats() { return null; }
							@Override
							public Collection<? extends Party> parties() { return null; }
						}
					);

					row.getValue().values().forEach(
						party -> {
							party.seats = output.parties().find(party).seats();
							party.uniqueness = output.parties().find(party).uniqueness();
						}
					);

					divisors.compute(row.getKey(), (key, divisor) -> divisorUpdateFunction.update(divisor, output.divisor(), faults.get(key)));
				}
			);

			// transer ties to minimize faults
			transfer();

			// update faults
			cols.forEach(
				(colKey, col) -> faults.put(
					colKey, col.values().stream()
					.map(BipropOutput.Party::seats)
					.reduce(Int::add).orElse(BMath.ZERO)
					.sub(seats.get(colKey))
				)
			);

			faultCount = faults.values().stream()
				.map(BMath.ZERO::max)
				.reduce(Int::add).orElse(BMath.ZERO);

			// flip rows / cols
			transpose();
		}

		return null;
	}


	private void transfer()
	{
		while (true)
		{
			Set<Object> Iminus = new HashSet<>(), Iplus = new HashSet<>();

			cols.forEach(
				(colKey, col) -> {
					Int sum = col.values().stream()
						.map(BipropOutput.Party::seats)
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
							&& party.votes.compareTo(0) > 0
							&& party.uniqueness == uniqueness)
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
				BipropOutput.Party party = cols.get(colKey).get(rowKey);
				party.seats = party.seats.sub(1);
				party.uniqueness = Uniqueness.CAN_BE_MORE;

				colKey = pre.get(rowKey);
				party = cols.get(colKey).get(rowKey);
				party.seats = party.seats.add(1);
				party.uniqueness = Uniqueness.CAN_BE_LESS;
			} while (!Iminus.contains(colKey));
		}
	}


	private void transpose()
	{
		Map<?, ? extends Map<?, BipropOutput.Party>> temp = rows;
		rows = cols;
		cols = temp;
	}
}
