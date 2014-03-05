package de.uni_augsburg.bazi.biprop;

import com.google.common.collect.ArrayTable;
import com.google.common.collect.Table;
import de.uni_augsburg.bazi.common.algorithm.*;
import de.uni_augsburg.bazi.common.data.Data;
import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Real;
import de.uni_augsburg.bazi.monoprop.Divisor;
import de.uni_augsburg.bazi.monoprop.DivisorAlgorithm;
import de.uni_augsburg.bazi.monoprop.DivisorOutput;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static de.uni_augsburg.bazi.biprop.BipropOutput.District;
import static de.uni_augsburg.bazi.common.algorithm.VectorOutput.Party;

public abstract class BipropAlgorithm implements MatrixAlgorithm
{
	@Override public List<Object> getInputAttributes()
	{
		return null;
	}


	@Override public MatrixOutput apply(MatrixInput in)
	{
		BipropOutput data = in.copy(BipropOutput.class);
		Table<District, String, Party> table = generateTable(data);

		Map<Object, Int> seats = data.districts().stream()
			.collect(
				Collectors.<District, Object, Int>toMap(
					Function.<District>identity(),
					District::seats
				)
			);

		DivisorOutput superApportionment = calculateSuperApportionment(table, seats.values());
		superApportionment.parties().forEach(party -> seats.put(party.name(), party.seats()));

		Map<Object, Real> divisors = calculate(table, seats);

		data.districts().forEach(d -> d.divisor(new Divisor(divisors.get(d), divisors.get(d))));
		data.partyDivisors(new LinkedHashMap<>());
		table.columnKeySet().forEach(
			name -> {
				System.out.println(name + " -> " + data.partyDivisors());
				data.partyDivisors().put(name, new Divisor(divisors.get(name), divisors.get(name)));
			}
		);
		return data;
	}


	private Table<District, String, Party> generateTable(BipropOutput out)
	{
		Set<String> names = out.districts().stream()
			.flatMap(district -> district.parties().stream())
			.map(Party::name)
			.collect(Collectors.toCollection(LinkedHashSet::new));

		Table<District, String, Party> table = ArrayTable.create(out.districts(), names);

		for (District district : out.districts())
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


	private DivisorOutput calculateSuperApportionment(Table<District, String, Party> table, Collection<Int> districtSeats)
	{
		Int superSeats = districtSeats.stream()
			.reduce(Int::add).orElse(BMath.ZERO);

		List<Party> superParties = table.columnMap().values().stream()
			.map(col -> mergeParties(col.values()))
			.collect(Collectors.toList());

		return divisorMethod().apply(
			new VectorInput()
			{
				@Override public String name() { return ""; }
				@Override public Int seats() { return superSeats; }
				@Override public List<? extends Party> parties() { return superParties; }
			}
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


	protected abstract DivisorAlgorithm divisorMethod();
	protected abstract Map<Object, Real> calculate(Table<District, String, Party> table, Map<Object, Int> seats);
}
