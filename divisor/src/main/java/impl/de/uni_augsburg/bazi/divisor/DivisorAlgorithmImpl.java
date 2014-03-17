package de.uni_augsburg.bazi.divisor;

import de.uni_augsburg.bazi.common.algorithm.Options;
import de.uni_augsburg.bazi.common.algorithm.VectorInput;
import de.uni_augsburg.bazi.common.algorithm.VectorOutput;
import de.uni_augsburg.bazi.common_vector.ShiftQueue;
import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Rational;
import de.uni_augsburg.bazi.math.Real;

import java.util.function.Supplier;

class DivisorAlgorithmImpl
{
	public static DivisorOutput calculate(VectorInput input, RoundingFunction r, String name, Options options)
	{
		DivisorOutput output = input.copy(DivisorOutput.class);
		try
		{
			Supplier<Int> seatsOff = () -> output.seats().sub(output.parties().stream().map(VectorOutput.Party::seats).reduce(Int::add).orElse(BMath.ZERO));

			calculateInitialSeats(output, r, options);

			ShiftQueue q = new ShiftQueue(output.parties(), r.getShiftFunction(options.precision()));
			q.shift(seatsOff.get());

			output.parties().forEach(
				p -> {
					if (p.seats().compareTo(p.min()) < 0) p.seats(p.min());
					else if (p.seats().compareTo(p.max()) > 0) p.seats(p.max());
					else return;
					p.conditionUsed(true);
				}
			);

			q = new ShiftQueue(output.parties(), r.getShiftFunction(options.precision()).mindConditions());
			q.shift(seatsOff.get());

			q.updateUniquenesses();
			output.divisor(new Divisor(q.nextIncreaseValue(), q.nextDecreaseValue()));
		}
		catch (ShiftQueue.NoShiftPossible e)
		{
			output.parties().forEach(p -> p.seats(BMath.NAN));
			output.divisor(new Divisor(BMath.NAN, BMath.NAN));
		}

		output.plain(new DivisorPlain(output, r, name));
		return output;
	}

	private static void calculateInitialSeats(DivisorOutput output, RoundingFunction r, Options options)
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
			Int s = r.round(party.votes().mul(ubm), options.precision());
			party.seats(s.max(party.min()).min(party.max()));
		}
	}
}
