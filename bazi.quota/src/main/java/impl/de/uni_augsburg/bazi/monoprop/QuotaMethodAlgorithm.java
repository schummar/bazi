package de.uni_augsburg.bazi.monoprop;

import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Rational;

import java.util.function.Supplier;

class QuotaMethodAlgorithm
{
	public static QuotaOutput calculate(MonopropInput input, QuotaFunction quotaFunction, ResidualHandler residualHandler)
	{
		QuotaOutput output = new QuotaOutput(input);
		Supplier<Int> seatsOff = () -> output.seats.sub(output.parties.stream().map(p -> p.seats).reduce((x, y) -> x.add(y)).get());

		Rational votes = output.parties.stream().map(p -> p.votes).reduce((x, y) -> x.add(y)).get();
		output.quota = quotaFunction.getQuota(votes, output.seats);

		for (QuotaOutput.Party party : output.parties)
		{
			Rational quotient = party.votes().div(output.quota);
			party.seats = quotient.floor();
		}

		ShiftQueue q = new ShiftQueue(output.parties, residualHandler.getShiftFunction(output.quota));
		q.shift(seatsOff.get());

		for (QuotaOutput.Party party : output.parties)
			party.seats = party.seats.max(party.min).min(party.max);

		q = new ShiftQueue(output.parties, residualHandler.getShiftFunction(output.quota).mindConditions());
		q.shift(seatsOff.get());

		q.updateUniquenesses();

		return output;
	}
}
