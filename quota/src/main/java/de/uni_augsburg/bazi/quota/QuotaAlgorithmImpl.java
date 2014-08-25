package de.uni_augsburg.bazi.quota;

import de.uni_augsburg.bazi.common.algorithm.VectorData;
import de.uni_augsburg.bazi.common_vector.ShiftQueue;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Real;

import java.util.function.Supplier;

class QuotaAlgorithmImpl
{
	public static void calculate(QuotaData data, QuotaFunction quotaFunction, ResidualHandler residualHandler, String name)
	{
		Supplier<Int> seatsOff = () -> data.seats().sub(data.parties().stream().map(VectorData.Party::seats).reduce(Int::add).get());

		Real votes = data.parties().stream().map(VectorData.Party::votes).reduce((x, y) -> x.add(y)).get();
		data.quota(quotaFunction.apply(votes, data.seats()));

		for (QuotaData.Party party : data.parties())
		{
			Real quotient = party.votes().div(data.quota());
			party.seats(quotient.floor());
		}

		ShiftQueue q = new ShiftQueue(data.parties(), residualHandler.getShiftFunction(data.quota()));
		q.shift(seatsOff.get());

		data.parties().forEach(
			p -> {
				if (p.seats().compareTo(p.min()) < 0) p.seats(p.min());
				else if (p.seats().compareTo(p.max()) > 0) p.seats(p.max());
				else return;
				p.conditionUsed(true);
			}
		);

		q = new ShiftQueue(data.parties(), residualHandler.getShiftFunction(data.quota()).mindConditions());
		q.shift(seatsOff.get());

		q.updateUniquenesses();
	}
}
