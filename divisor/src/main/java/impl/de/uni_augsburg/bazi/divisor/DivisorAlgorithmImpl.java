package de.uni_augsburg.bazi.divisor;

import de.uni_augsburg.bazi.common.algorithm.VectorInput;
import de.uni_augsburg.bazi.common.algorithm.VectorOutput;
import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Rational;
import de.uni_augsburg.bazi.math.Real;
import de.uni_augsburg.bazi.monoprop.ShiftQueue;

import java.util.function.Supplier;

class DivisorAlgorithmImpl
{
	public static DivisorOutput calculate(VectorInput input, RoundingFunction r, int minPrecision, String name)
	{
		DivisorOutput output = input.copy(DivisorOutput.class);
		try
		{
			Supplier<Int> seatsOff = () -> output.seats().sub(output.parties().stream().map(VectorOutput.Party::seats).reduce(Int::add).orElse(BMath.ZERO));

			calculateInitialSeats(output, r, minPrecision);

			ShiftQueue q = new ShiftQueue(output.parties(), r.getShiftFunction(minPrecision));
			q.shift(seatsOff.get());

			for (DivisorOutput.Party party : output.parties())
				party.seats(party.seats().max(party.min()).min(party.max()));

			q = new ShiftQueue(output.parties(), r.getShiftFunction(minPrecision).mindConditions());
			q.shift(seatsOff.get());

			q.updateUniquenesses();
			output.divisor(new Divisor(q.nextIncreaseValue(), q.nextDecreaseValue()));

			if (output.name() == null) output.name(name);
			output.plain(new DivisorPlain(output, r));
			return output;
		}
		catch (ShiftQueue.NoShiftPossible e)
		{
			output.parties().forEach(p -> p.seats(BMath.NAN));
			output.divisor(new Divisor(BMath.NAN, BMath.NAN));
			return output;
		}
	}

	private static void calculateInitialSeats(DivisorOutput output, RoundingFunction r, int minPrecision)
	{
		/* Erste Zuteilung der Sitze gemaess der Min-Bedingung und der Zuteilung mit Hilfe des Unbiased Multipliers
		 * Stationaere Methoden erlauben noch eine Verbesserung */
		Real ubm = output.seats();
		if (output.seats().compareTo(output.parties().size() / 2) > 0 && r instanceof RoundingFunction.Stationary)
		{
			Rational param = r.getParam();
			ubm = output.seats().add(param.sub(BMath.HALF).div(output.parties().size()));
		}

		Real votes = output.parties().stream().map(VectorOutput.Party::votes).reduce(Real::add).orElse(BMath.ZERO);
		ubm = ubm.div(votes);

		for (DivisorOutput.Party party : output.parties())
		{
			Int s = r.round(party.votes().mul(ubm), minPrecision);
			party.seats(s.max(party.min()).min(party.max()));
		}
	}
}
