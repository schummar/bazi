package de.uni_augsburg.bazi.divisor;

import java.util.Arrays;
import java.util.List;

import de.uni_augsburg.bazi.common.Json;
import de.uni_augsburg.bazi.divisor.Output.Party;
import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Real;
import de.uni_augsburg.bazi.monoprop.InDecreaseQueue;
import de.uni_augsburg.bazi.monoprop.MonopropMethod;
import de.uni_augsburg.bazi.monoprop.Uniqueness;

public class PriorityQueueTest
{
	private static class Comp implements InDecreaseQueue.Comp
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
			int comp = r0.compareTo(r1);
			if (comp != 0)
				return comp;
			return bias(p0, s0) - bias(p1, s1);
		}

		@Override public boolean canHaveSeats(MonopropMethod.Input.Party p, Int s)
		{
			return s.compareTo(p.getMin()) >= 0 && s.compareTo(p.getMax()) <= 0;
		}

		private static int bias(MonopropMethod.Input.Party p, Int s)
		{
			if (s.sub(1).compareTo(p.getMin()) < 0)
				return 1;
			if (s.compareTo(p.getMax()) > 0)
				return -1;
			return 0;
		}
	}


	public static void main(String[] args)
	{
		Int allSeats = BMath.valueOf(4);
		// final Int[] seats = { BMath.ZERO, BMath.ZERO, BMath.ZERO };
		// final Int[] min = { BMath.valueOf(0), BMath.valueOf(0), BMath.valueOf(0) };
		// final Rational[] weights = { BMath.valueOf("1"), BMath.valueOf("1"), BMath.valueOf("1") };
		final RoundingFunction.Stationary r = new RoundingFunction.Stationary(BMath.HALF, null);

		List<Party> parties = Arrays.asList(
				new Party("A", BMath.valueOf("1"), BMath.valueOf(0), BMath.valueOf(10), BMath.ZERO, BMath.ZERO, BMath.ZERO, null),
				new Party("B", BMath.valueOf("1"), BMath.valueOf(0), BMath.valueOf(10), BMath.ZERO, BMath.ZERO, BMath.ZERO, null),
				new Party("C", BMath.valueOf("1"), BMath.valueOf(0), BMath.valueOf(10), BMath.ZERO, BMath.ZERO, BMath.ZERO, null)
				);
		Int[] seats = new Int[parties.size()];
		Uniqueness[] uniquenesses = new Uniqueness[parties.size()];

		InDecreaseQueue q = new InDecreaseQueue(parties, seats, new Comp(r));
		q.increase(allSeats);


		for (Int sum = BMath.ZERO; sum.compareTo(allSeats) < 0; sum = sum.add(1))
		{
			Party next = increasePQ.next(parties);
			next.seats = next.seats.add(1);
		}

		Party lastIncreased = decreasePQ.next(parties);
		Party lastDecreased = increasePQ.next(parties);
		for (Party p : parties)
			if (increasePQ.isTiedWithNext(p, parties))
				p.uniqueness = Uniqueness.CAN_BE_LESS;
			else if (decreasePQ.isTiedWithNext(p, parties))
				p.uniqueness = Uniqueness.CAN_BE_MORE;
			else
				p.uniqueness = Uniqueness.UNIQUE;

		System.out.println(Json.toJson(parties));
	}
}
