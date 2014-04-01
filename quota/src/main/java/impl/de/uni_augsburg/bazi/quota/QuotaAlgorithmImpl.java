package de.uni_augsburg.bazi.quota;

import de.uni_augsburg.bazi.common.algorithm.VectorInput;
import de.uni_augsburg.bazi.common.algorithm.VectorOutput;
import de.uni_augsburg.bazi.common_vector.ShiftQueue;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Real;

import java.util.function.Supplier;

class QuotaAlgorithmImpl
{
	public static QuotaOutput calculate(VectorInput input, QuotaFunction quotaFunction, ResidualHandler residualHandler, String name)
	{
		QuotaOutput output = input.copy(QuotaOutput.class);
		Supplier<Int> seatsOff = () -> output.seats().sub(output.parties().stream().map(VectorOutput.Party::seats).reduce(Int::add).get());

		Real votes = output.parties().stream().map(VectorOutput.Party::votes).reduce((x, y) -> x.add(y)).get();
		output.quota(quotaFunction.apply(votes, output.seats()));

		for (QuotaOutput.Party party : output.parties())
		{
			Real quotient = party.votes().div(output.quota());
			party.seats(quotient.floor());
		}

		ShiftQueue q = new ShiftQueue(output.parties(), residualHandler.getShiftFunction(output.quota()));
		q.shift(seatsOff.get());

		output.parties().forEach(
			p -> {
				if (p.seats().compareTo(p.min()) < 0) p.seats(p.min());
				else if (p.seats().compareTo(p.max()) > 0) p.seats(p.max());
				else return;
				p.conditionUsed(true);
			}
		);

		q = new ShiftQueue(output.parties(), residualHandler.getShiftFunction(output.quota()).mindConditions());
		q.shift(seatsOff.get());

		q.updateUniquenesses();

		output.plain(new QuotaPlain(output, name));
		return output;
	}
}
