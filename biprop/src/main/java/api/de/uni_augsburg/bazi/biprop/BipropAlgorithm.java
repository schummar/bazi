package de.uni_augsburg.bazi.biprop;

import com.google.common.collect.ArrayTable;
import com.google.common.collect.Table;
import de.schummar.castable.Data;
import de.uni_augsburg.bazi.common.algorithm.Algorithm;
import de.uni_augsburg.bazi.common.algorithm.MatrixAlgorithm;
import de.uni_augsburg.bazi.common.algorithm.MatrixData;
import de.uni_augsburg.bazi.common.algorithm.MatrixOutput;
import de.uni_augsburg.bazi.common.algorithm.Options;
import de.uni_augsburg.bazi.common.algorithm.VectorData;
import de.uni_augsburg.bazi.common.data.Data;
import de.uni_augsburg.bazi.divisor.Divisor;
import de.uni_augsburg.bazi.divisor.DivisorAlgorithm;
import de.uni_augsburg.bazi.divisor.DivisorData;
import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Real;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static de.uni_augsburg.bazi.common.algorithm.VectorData.Party;

/**
 * A biproportional algorithm calculates a set of row and column divisors that solve a two dimensional
 * apportionment problem.
 */
public abstract class BipropAlgorithm implements Algorithm
{
	@Override public List<Object> getInputAttributes()
	{
		return null;
	}


	@Override public void applyUnfiltered(Data data, Options options)
	{
		BipropData bipropData = data.cast(BipropData.class);
		Table<DivisorData, String, Party> table = generateTable(bipropData);

		Map<Object, Int> seats = bipropData.districts().stream()
			.collect(
				Collectors.<DivisorData, Object, Int>toMap(
					Function.<DivisorData>identity(),
					DivisorData::seats
				)
			);

		DivisorData superApportionment = calculateSuperApportionment(table, seats.values(), options);
		superApportionment.parties().forEach(party -> seats.put(party.name(), party.seats()));

		Map<Object, Real> divisors = calculate(table, seats, options);

		bipropData.superApportionment(superApportionment);
		bipropData.districts().forEach(d -> d.divisor(new Divisor(divisors.get(d), divisors.get(d))));
		table.columnKeySet().forEach(name -> bipropData.partyDivisors().put(name, new Divisor(divisors.get(name), divisors.get(name))));
		//bipropData.plain(new BipropPlain(data, sub().name()));
	}


	private Table<DivisorData, String, Party> generateTable(BipropData out)
	{
		Set<String> names = out.districts().stream()
			.flatMap(district -> district.parties().stream())
			.map(Party::name)
			.collect(Collectors.toCollection(LinkedHashSet::new));

		Table<DivisorData, String, Party> table = ArrayTable.create(out.districts(), names);

		for (DivisorData district : out.districts())
			for (String name : names)
			{
				Party party = district.parties().stream()
					.filter(p -> p.name().equals(name))
					.findAny().orElse(null);
				if (party == null) party = ((VectorData.Party) () -> name).cast(Party.class);
				party.seats(BMath.ZERO);
				table.put(district, name, party);
			}

		return table;
	}


	private DivisorData calculateSuperApportionment(Table<DivisorData, String, Party> table, Collection<Int> districtSeats, Options options)
	{
		Int superSeats = districtSeats.stream()
			.reduce(Int::add).orElse(BMath.ZERO);

		List<Party> superParties = table.columnMap().values().stream()
			.map(col -> mergeParties(col.values()))
			.collect(Collectors.toList());

		return Super().apply(
			new VectorData()
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
		if (parties.size() == 0) return ((VectorData.Party) () -> "").cast(Party.class);

		Party party = Data.create(Party.class);
		party.name(parties.stream().findAny().get().name());
		party.votes(parties.stream().map(Party::votes).reduce(Real::add).get());
		party.min(parties.stream().map(Party::min).reduce(Int::add).get());
		party.max(parties.stream().map(Party::max).reduce(Int::add).get());
		return party;
	}


	protected abstract DivisorAlgorithm Super();
	protected abstract DivisorAlgorithm sub();
	protected abstract Map<Object, Real> calculate(Table<DivisorData, String, Party> table, Map<Object, Int> seats, Options options);
}
