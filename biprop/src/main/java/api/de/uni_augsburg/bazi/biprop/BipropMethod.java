package de.uni_augsburg.bazi.biprop;

import com.google.common.collect.ArrayTable;
import com.google.common.collect.Table;
import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.monoprop.Divisor;
import de.uni_augsburg.bazi.monoprop.DivisorMethod;
import de.uni_augsburg.bazi.monoprop.DivisorOutput;
import de.uni_augsburg.bazi.monoprop.MonopropInput;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class BipropMethod
{
	public BipropOutput calculate(BipropInput input)
	{
		Table<BipropInput.District, String, BipropOutput.Party> table = generateTable(input);

		Map<Object, Int> seats = input.districts().stream()
			.collect(
				Collectors.<BipropInput.District, Object, Int>toMap(
					Function.<BipropInput.District>identity(),
					BipropInput.District::seats
				)
			);

		DivisorOutput superApportionment = calculateSuperApportionment(table, seats.values());
		superApportionment.parties().forEach(party -> seats.put(party.name(), party.seats()));

		Map<Object, Divisor> divisors = calculate(table, seats);

		return new BipropOutput(superApportionment, table, seats, divisors);
	}


	private Table<BipropInput.District, String, BipropOutput.Party> generateTable(BipropInput input)
	{
		Set<String> names = input.districts().stream()
			.flatMap(district -> district.parties().stream())
			.map(MonopropInput.Party::name)
			.collect(Collectors.toCollection(LinkedHashSet::new));

		Table<BipropInput.District, String, BipropOutput.Party> table = ArrayTable.create(input.districts(), names);

		input.districts().forEach(
			district -> names.forEach(
				name -> {
					BipropOutput.Party party = district.parties().stream()
						.filter(p -> p.name().equals(name))
						.map(BipropOutput.Party::new)
						.findAny().orElse(new BipropOutput.Party(name));

					table.put(district, name, party);
				}
			)
		);

		return table;
	}


	private DivisorOutput calculateSuperApportionment(Table<BipropInput.District, String, BipropOutput.Party> table, Collection<Int> districtSeats)
	{
		Int superSeats = districtSeats.stream()
			.reduce(Int::add).orElse(BMath.ZERO);

		List<MonopropInput.Party> superParties = table.columnMap().values().stream()
			.map(col -> col.values().stream().reduce(this::mergeParties).get())
			.collect(Collectors.toList());

		return divisorMethod().calculate(MonopropInput.create(superSeats, superParties));
	}


	private BipropOutput.Party mergeParties(BipropOutput.Party p0, BipropOutput.Party p1)
	{
		BipropOutput.Party p = new BipropOutput.Party(p0.name());
		p.votes = p0.votes.add(p1.votes);
		p.min = p0.min.add(p1.min);
		p.max = p0.max.add(p1.max);
		p.dir = p1.dir.add(p1.dir);
		return p;
	}


	private Map<Object,Divisor> calculateDivisors(Table<BipropInput.District, String, BipropOutput.Party> table)
	{

	}


	protected abstract DivisorMethod divisorMethod();

	protected abstract Map<Object, Divisor> calculate(Table<BipropInput.District, String, BipropOutput.Party> table, Map<Object, Int> seats);
}
