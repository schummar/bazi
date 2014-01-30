package de.uni_augsburg.bazi.divisor;

import java.util.ArrayList;
import java.util.List;

import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Rational;
import de.uni_augsburg.bazi.math.Real;
import de.uni_augsburg.bazi.monoprop.MonopropMethod;
import de.uni_augsburg.bazi.monoprop.MonopropMethod.Input;
import de.uni_augsburg.bazi.monoprop.ShiftQueue;
import de.uni_augsburg.bazi.monoprop.ShiftQueue.NoShiftPossible;
import de.uni_augsburg.bazi.monoprop.Uniqueness;

class DivisorMethodImpl
{
	public static DivisorMethod.Output calculate(RoundingFunction r, MonopropMethod.Input input, int minPrecision)
	{
		try
		{
			List<? extends Input.Party> parties = input.parties();
			Int allSeats = input.seats();

			List<Int> seats = getInitialSeats(input.parties(), input.seats(), r, minPrecision);
			List<Uniqueness> uniquenesses;
			Real dmin, dmax;

			ShiftQueue q = new ShiftQueue(parties, seats, (p, s) -> shiftFunction(p, s, r, false, minPrecision));
			q.shift(allSeats.sub(seats.stream().reduce((x, y) -> x.add(y)).get()));

			for (int i = 0; i < parties.size(); i++)
			{
				if (seats.get(i).compareTo(parties.get(i).min()) < 0)
					seats.set(i, parties.get(i).min());
				if (seats.get(i).compareTo(parties.get(i).max()) > 0)
					seats.set(i, parties.get(i).max());
			}

			q = new ShiftQueue(parties, seats, (p, s) -> shiftFunction(p, s, r, true, minPrecision));
			q.shift(allSeats.sub(seats.stream().reduce((x, y) -> x.add(y)).get()));


			uniquenesses = q.getUniquenesses();

			int nextIncrease = q.nextIncrease(), nextDecrease = q.nextDecrease();
			dmin = parties.get(nextIncrease).votes().div(r.getBorder(seats.get(nextIncrease), minPrecision));
			dmax = parties.get(nextDecrease).votes().div(r.getBorder(seats.get(nextDecrease).sub(1), minPrecision));

			List<Output.Party> outParties = new ArrayList<>();
			for (int i = 0; i < parties.size(); i++)
			{
				Input.Party p = parties.get(i);
				outParties.add(new Output.Party(p.name(), p.votes(), p.min(), p.max(), p.dir(), null, seats.get(i), uniquenesses.get(i)));
			}
			return new Output(allSeats, outParties, new Divisor(dmin, dmax));
		}
		catch (NoShiftPossible e)
		{
			return null;
		}
	}
	private static List<Int> getInitialSeats(List<? extends Input.Party> parties, Int seats, RoundingFunction r, int minPrecision)
	{
		/* Erste Zuteilung der Sitze gemaess der Min-Bedingung und der Zuteilung mit Hilfe des Unbiased Multipliers
		 * Stationaere Methoden erlauben noch eine Verbesserung */
		Rational ubm = seats;
		if (seats.compareTo(parties.size() / 2) > 0 && r instanceof RoundingFunction.Stationary)
		{
			Rational param = ((RoundingFunction.Stationary) r).getParam();
			ubm = seats.add(param.sub(BMath.HALF).div(parties.size()));
		}

		Rational voteSum = BMath.ZERO;
		for (Input.Party party : parties)
			voteSum = voteSum.add(party.votes());
		ubm = ubm.div(voteSum);

		List<Int> pseats = new ArrayList<>();
		for (Input.Party party : parties)
		{
			Int s = r.round(party.votes().mul(ubm), minPrecision);
			s = s.max(party.min()).min(party.max());
			pseats.add(s);
		}
		return pseats;
	}

	private static Real shiftFunction(MonopropMethod.Input.Party p, Int s, RoundingFunction r, boolean mindConditions, int minPrecision)
	{
		if (mindConditions && s.compareTo(p.min()) < 0)
			return BMath.INF;
		if (mindConditions && s.compareTo(p.max()) > 0)
			return BMath.ZERO;
		Real border = r.getBorder(s, minPrecision);
		if (border.sgn() <= 0)
			return BMath.INF;
		return p.votes().div(border);
	}
}
