package de.uni_augsburg.bazi.biprop;

import com.google.common.collect.ArrayTable;
import com.google.common.collect.Table;
import de.schummar.castable.Data;
import de.uni_augsburg.bazi.common.StringTable;
import de.uni_augsburg.bazi.common.algorithm.Algorithm;
import de.uni_augsburg.bazi.common.algorithm.Options;
import de.uni_augsburg.bazi.common.plain.PlainOptions;
import de.uni_augsburg.bazi.divisor.Divisor;
import de.uni_augsburg.bazi.divisor.DivisorData;
import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Real;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import static de.uni_augsburg.bazi.common.algorithm.VectorData.Party;

/**
 * A biproportional algorithm calculates a set of row and column divisors that solve a two dimensional
 * apportionment problem.
 */
public abstract class BipropAlgorithm<T extends BipropData> implements Algorithm<T>
{
	@Override public BiFunction<Data, PlainOptions, List<StringTable>> plainFormatter()
	{
		return (data, options) -> new BipropPlain(data.cast(BipropData.class), options, Super(), sub().name()).get();
	}

	@Override public void apply(Data data, Options options)
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
				if (party == null)
				{
					party = Data.create(Party.class);
					party.name(name);
				}
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

		DivisorData superApportionment = Data.create(DivisorData.class);
		superApportionment.seats(superSeats);
		superApportionment.parties().setAllData(superParties);

		Super().apply(superApportionment, options);
		return superApportionment;
	}


	private Party mergeParties(Collection<Party> parties)
	{
		Party party = Data.create(Party.class);
		if (parties.size() == 0) return party;

		party.name(parties.stream().findAny().get().name());
		party.votes(parties.stream().map(Party::votes).reduce(Real::add).get());
		party.min(parties.stream().map(Party::min).reduce(Int::add).get());
		party.max(parties.stream().map(Party::max).reduce(Int::add).get());
		return party;
	}


	public abstract Algorithm<? extends DivisorData> Super();
	public abstract Algorithm<? extends DivisorData> sub();
	protected abstract Map<Object, Real> calculate(Table<DivisorData, String, Party> table, Map<Object, Int> seats, Options options);
}
