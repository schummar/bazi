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

			ShiftQueue q = new ShiftQueue(parties, seats, new Comp(r));
			q.shift(allSeats.sub(BMath.sumInt(seats)));

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
				q = new ShiftQueue(parties, seats, new CompStrict(r));
				q.shift(allSeats.sub(BMath.sumInt(seats)));
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
			s = s.min(party.getMin()).max(party.getMax());
			pseats.add(s);
		}
		return pseats;
	}

	private static class Comp implements ShiftQueue.Comp
	{
		private final RoundingFunction r;

		public Comp(RoundingFunction r)
		{
			this.r = r;
		}

		@Override public int compare(MonopropMethod.Input.Party p0, Int s0, MonopropMethod.Input.Party p1, Int s1)
		{
			Real r0 = p0.getVotes().div(r.getBorder(s0));
			Real r1 = p1.getVotes().div(r.getBorder(s1));
			return r0.compareTo(r1);
		}
	}

	private static class CompStrict implements ShiftQueue.Comp
	{
		private final RoundingFunction r;

		public CompStrict(RoundingFunction r)
		{
			this.r = r;
		}

		@Override public int compare(MonopropMethod.Input.Party p0, Int s0, MonopropMethod.Input.Party p1, Int s1)
		{
			Real r0, r1;

			if (s0.compareTo(p0.getMin()) < 0)
				r0 = BMath.INF;
			else if (s0.compareTo(p0.getMax()) > 0)
				r0 = BMath.ZERO;
			else
				r0 = p0.getVotes().div(r.getBorder(s0));

			if (s1.compareTo(p1.getMin()) < 0)
				r1 = BMath.INF;
			else if (s1.compareTo(p1.getMax()) > 0)
				r1 = BMath.ZERO;
			else
				r1 = p1.getVotes().div(r.getBorder(s1));
			return r0.compareTo(r1);
		}
	}
}
