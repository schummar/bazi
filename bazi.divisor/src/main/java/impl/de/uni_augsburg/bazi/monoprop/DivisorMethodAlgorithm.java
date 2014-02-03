package de.uni_augsburg.bazi.monoprop;

import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Rational;
import de.uni_augsburg.bazi.monoprop.ShiftQueue.NoShiftPossible;

import java.util.function.Supplier;

class DivisorMethodAlgorithm
{
	public static DivisorOutput calculate(MonopropInput input, RoundingFunction r, int minPrecision)
	{
		try
		{
			DivisorOutput output = new DivisorOutput(input);
			Supplier<Int> seatsOff = () -> output.seats.sub(output.parties.stream().map(p -> p.seats).reduce((x, y) -> x.add(y)).get());

			calculateInitialSeats(output, r, minPrecision);

			ShiftQueue q = new ShiftQueue(output.parties, r.getShiftFunction(minPrecision));
			q.shift(seatsOff.get());

			for (DivisorOutput.Party party : output.parties)
				party.seats = party.seats.max(party.min).min(party.max);

			q = new ShiftQueue(output.parties, r.getShiftFunction(minPrecision).mindConditions());
			q.shift(seatsOff.get());

			q.updateUniquenesses();
			output.divisor = new Divisor(q.nextIncreaseValue(), q.nextDecreaseValue());

			return output;
		}
		catch (NoShiftPossible e)
		{
			return null;
		}
	}

	private static void calculateInitialSeats(DivisorOutput output, RoundingFunction r, int minPrecision)
	{
		/* Erste Zuteilung der Sitze gemaess der Min-Bedingung und der Zuteilung mit Hilfe des Unbiased Multipliers
		 * Stationaere Methoden erlauben noch eine Verbesserung */
		Rational ubm = output.seats;
		if (output.seats.compareTo(output.parties.size() / 2) > 0 && r instanceof RoundingFunction.Stationary)
		{
			Rational param = ((RoundingFunction.Stationary) r).getParam();
			ubm = output.seats.add(param.sub(BMath.HALF).div(output.parties.size()));
		}

		Rational votes = output.parties.stream().map(p -> p.votes).reduce((a, b) -> a.add(b)).get();
		ubm = ubm.div(votes);

		for (DivisorOutput.Party party : output.parties)
		{
			Int s = r.round(party.votes().mul(ubm), minPrecision);
			party.seats = s.max(party.min()).min(party.max());
		}
	}
}