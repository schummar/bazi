package de.uni_augsburg.bazi.biprop;

import com.google.common.collect.ArrayTable;
import com.google.common.collect.Table;
import de.uni_augsburg.bazi.common.algorithm.MatrixAlgorithm;
import de.uni_augsburg.bazi.common.algorithm.MatrixOutput;
import de.uni_augsburg.bazi.common.algorithm.Options;
import de.uni_augsburg.bazi.common.algorithm.VectorInput;
import de.uni_augsburg.bazi.common.data.Data;
import de.uni_augsburg.bazi.divisor.Divisor;
import de.uni_augsburg.bazi.divisor.DivisorAlgorithm;
import de.uni_augsburg.bazi.divisor.DivisorOutput;
import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Real;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static de.uni_augsburg.bazi.common.algorithm.VectorOutput.Party;

/**
 * A biproportional algorithm calculates a set of row and column divisors that solve a two dimensional
 * apportionment problem.
 */
public abstract class BipropAlgorithm implements MatrixAlgorithm<MatrixOutput>
{
	@Override public List<Object> getInputAttributes()
	{
		return null;
	}


	@Override public MatrixOutput applyUnfiltered(Data in, Options options)
	{
		BipropOutput data = in.copy(BipropOutput.class);
		Table<DivisorOutput, String, Party> table = generateTable(data);

		Map<Object, Int> seats = data.districts().stream()
			.collect(
				Collectors.<DivisorOutput, Object, Int>toMap(
					Function.<DivisorOutput>identity(),
					DivisorOutput::seats
				)
			);

		DivisorOutput superApportionment = calculateSuperApportionment(table, seats.values(), options);
		superApportionment.parties().forEach(party -> seats.put(party.name(), party.seats()));

		Map<Object, Real> divisors = calculate(table, seats, options);

		data.superApportionment(superApportionment);
		data.districts().forEach(d -> d.divisor(new Divisor(divisors.get(d), divisors.get(d))));
		data.partyDivisors(new LinkedHashMap<>());
		table.columnKeySet().forEach(name -> data.partyDivisors().put(name, new Divisor(divisors.get(name), divisors.get(name))));
		data.plain(new BipropPlain(data, sub().name()));
		return data;
	}


	private Table<DivisorOutput, String, Party> generateTable(BipropOutput out)
	{
		Set<String> names = out.districts().stream()
			.flatMap(district -> district.parties().stream())
			.map(Party::name)
			.collect(Collectors.toCollection(LinkedHashSet::new));

		Table<DivisorOutput, String, Party> table = ArrayTable.create(out.districts(), names);

		for (DivisorOutput district : out.districts())
			for (String name : names)
			{
				Party party = district.parties().stream()
					.filter(p -> p.name().equals(name))
					.findAny().orElse(null);
				if (party == null) party = ((VectorInput.Party) () -> name).cast(Party.class);
				party.seats(BMath.ZERO);
				table.put(district, name, party);
			}

		return table;
	}


	private DivisorOutput calculateSuperApportionment(Table<DivisorOutput, String, Party> table, Collection<Int> districtSeats, Options options)
	{
		Int superSeats = districtSeats.stream()
			.reduce(Int::add).orElse(BMath.ZERO);

		List<Party> superParties = table.columnMap().values().stream()
			.map(col -> mergeParties(col.values()))
			.collect(Collectors.toList());

		return Super().apply(
			new VectorInput()
			{
				@Override public String name() { return ""; }
				@Override public Int seats() { return superSeats; }
				@Override public List<? extends Party> parties() { return superParties; }
			},
			options
		);
	}


	private Party mergeParties(Collection<Party> parties)
	{
		if (parties.size() == 0) return ((VectorInput.Party) () -> "").cast(Party.class);

		Party party = Data.create(Party.class);
		party.name(parties.stream().findAny().get().name());
		party.votes(parties.stream().map(Party::votes).reduce(Real::add).get());
		party.min(parties.stream().map(Party::min).reduce(Int::add).get());
		party.max(parties.stream().map(Party::max).reduce(Int::add).get());
		return party;
	}


	protected abstract DivisorAlgorithm Super();
	protected abstract DivisorAlgorithm sub();
	protected abstract Map<Object, Real> calculate(Table<DivisorOutput, String, Party> table, Map<Object, Int> seats, Options options);
}
