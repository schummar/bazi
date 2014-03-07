package de.uni_augsburg.bazi.quota;

import de.uni_augsburg.bazi.common.algorithm.VectorInput;
import de.uni_augsburg.bazi.common.algorithm.VectorOutput;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Real;
import de.uni_augsburg.bazi.monoprop.ShiftQueue;

import java.util.function.Supplier;

class QuotaAlgorithmImpl
{
	public static QuotaOutput calculate(VectorInput input, QuotaFunction quotaFunction, ResidualHandler residualHandler, String name)
	{
		QuotaOutput output = input.copy(QuotaOutput.class);
		Supplier<Int> seatsOff = () -> output.seats().sub(output.parties().stream().map(VectorOutput.Party::seats).reduce(Int::add).get());

		Real votes = output.parties().stream().map(VectorOutput.Party::votes).reduce((x, y) -> x.add(y)).get();
		output.quota(quotaFunction.getQuota(votes, output.seats()));

		for (QuotaOutput.Party party : output.parties())
		{
			Real quotient = party.votes().div(output.quota());
			party.seats(quotient.floor());
		}

		ShiftQueue q = new ShiftQueue(output.parties(), residualHandler.getShiftFunction(output.quota()));
		q.shift(seatsOff.get());

		for (QuotaOutput.Party party : output.parties())
			party.seats(party.seats().max(party.min()).min(party.max()));

		q = new ShiftQueue(output.parties(), residualHandler.getShiftFunction(output.quota()).mindConditions());
		q.shift(seatsOff.get());

		q.updateUniquenesses();

		if (output.name() == null) output.name(name);
		output.plain(new QuotaPlain(output));
		return output;
	}
}
