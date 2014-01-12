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
	public static DivisorMethod.Output calculate(RoundingFunction r, MonopropMethod.Input input)
	{
		try
		{
			List<? extends Input.Party> parties = input.getParties();
			Int allSeats = input.getSeats();

			List<Int> seats = getInitialSeats(input.getParties(), input.getSeats(), r);
			List<Uniqueness> uniquenesses;
			Real dmin, dmax;

			ShiftQueue q = new ShiftQueue(parties, seats, (p0, s0, p1, s1) -> compare(p0, s0, p1, s1, r, false));
			q.shift(allSeats.sub(seats.stream().reduce((x, y) -> x.add(y)).get()));

			boolean allOK = true;
			for (int i = 0; i < parties.size(); i++)
			{
				if (seats.get(i).compareTo(parties.get(i).getMin()) < 0)
				{
					allOK = false;
					seats.set(i, parties.get(i).getMin());
				}
				if (seats.get(i).compareTo(parties.get(i).getMax()) > 0)
				{
					allOK = false;
					seats.set(i, parties.get(i).getMax());
				}
			}

			if (!allOK)
			{
				q = new ShiftQueue(parties, seats, (p0, s0, p1, s1) -> compare(p0, s0, p1, s1, r, true));
				q.shift(allSeats.sub(seats.stream().reduce((x, y) -> x.add(y)).get()));
			}

			uniquenesses = q.getUniquenesses();

			int nextIncrease = q.nextIncrease(), nextDecrease = q.nextDecrease();
			dmin = parties.get(nextIncrease).getVotes().div(r.getBorder(seats.get(nextIncrease)));
			dmax = parties.get(nextDecrease).getVotes().div(r.getBorder(seats.get(nextDecrease).sub(1)));

			List<Output.Party> outParties = new ArrayList<>();
			for (int i = 0; i < parties.size(); i++)
			{
				Input.Party p = parties.get(i);
				outParties.add(new Output.Party(p.getName(), p.getVotes(), p.getMin(), p.getMax(), p.getDir(), null, seats.get(i), uniquenesses.get(i)));
			}
			return new Output(allSeats, outParties, dmin);
		}
		catch (NoShiftPossible e)
		{
			return null;
		}
	}
	private static List<Int> getInitialSeats(List<? extends Input.Party> parties, Int seats, RoundingFunction r)
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
			voteSum = voteSum.add(party.getVotes());
		ubm = ubm.div(voteSum);

		List<Int> pseats = new ArrayList<>();
		for (Input.Party party : parties)
		{
			Int s = r.round(party.getVotes().mul(ubm));
			s = s.max(party.getMin()).min(party.getMax());
			pseats.add(s);
		}
		return pseats;
	}

	private static Real getShiftValue(MonopropMethod.Input.Party p, Int s, RoundingFunction r, boolean mindConditions)
	{
		if (mindConditions && s.compareTo(p.getMin()) < 0)
			return BMath.INF;
		if (mindConditions && s.compareTo(p.getMax()) > 0)
			return BMath.ZERO;
		Real border = r.getBorder(s);
		if (border.sgn() <= 0)
			return BMath.INF;
		return p.getVotes().div(border);
	}

	private static int compare(MonopropMethod.Input.Party p0, Int s0, MonopropMethod.Input.Party p1, Int s1, RoundingFunction r, boolean mindConditions)
	{
		return getShiftValue(p0, s0, r, mindConditions).compareTo(getShiftValue(p1, s1, r, mindConditions));
	}
}
