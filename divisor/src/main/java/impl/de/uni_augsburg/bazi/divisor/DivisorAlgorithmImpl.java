package de.uni_augsburg.bazi.divisor;

import de.uni_augsburg.bazi.common.algorithm.Options;
import de.uni_augsburg.bazi.common.algorithm.VectorData;
import de.uni_augsburg.bazi.common_vector.ShiftQueue;
import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Rational;
import de.uni_augsburg.bazi.math.Real;

import java.util.function.Supplier;

class DivisorAlgorithmImpl
{
	public static void calculate(DivisorData data, RoundingFunction r, String name, Options options)
	{
		try
		{
			Supplier<Int> seatsOff = () -> data.seats().sub(data.parties().stream().map(VectorData.Party::seats).reduce(Int::add).orElse(BMath.ZERO));

			calculateInitialSeats(data, r, options);

			ShiftQueue q = new ShiftQueue(data.parties(), r.getShiftFunction(options.precision()));
			q.shift(seatsOff.get());

			data.parties().forEach(
				p -> {
					if (p.seats().compareTo(p.min()) < 0) p.seats(p.min());
					else if (p.seats().compareTo(p.max()) > 0) p.seats(p.max());
					else return;
					p.conditionUsed(true);
				}
			);

			q = new ShiftQueue(data.parties(), r.getShiftFunction(options.precision()).mindConditions());
			q.shift(seatsOff.get());

			q.updateUniquenesses();
			data.divisor(new Divisor(q.nextIncreaseValue(), q.nextDecreaseValue()));
		}
		catch (ShiftQueue.NoShiftPossible e)
		{
			data.parties().forEach(p -> p.seats(BMath.NAN));
			data.divisor(new Divisor(BMath.NAN, BMath.NAN));
		}

		//data.plain(new DivisorPlain(data, r, name));
	}

	private static void calculateInitialSeats(DivisorData output, RoundingFunction r, Options options)
	{
		/* Erste Zuteilung der Sitze gemaess der Min-Bedingung und der Zuteilung mit Hilfe des Unbiased Multipliers
		 * Stationaere Methoden erlauben noch eine Verbesserung */
		Real ubm = output.seats();
		if (output.seats().compareTo(output.parties().size() / 2) > 0 && r instanceof RoundingFunction.Stationary)
		{
			Rational param = r.getParam();
			ubm = output.seats().add(param.sub(BMath.HALF).div(output.parties().size()));
		}

		Real votes = output.parties().stream().map(VectorData.Party::votes).reduce(Real::add).orElse(BMath.ZERO);
		ubm = ubm.div(votes);

		for (DivisorData.Party party : output.parties())
		{
			Int s = r.round(party.votes().mul(ubm), options.precision());
			party.seats(s.max(party.min()).min(party.max()));
		}
	}
}
