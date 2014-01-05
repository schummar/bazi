package de.uni_augsburg.bazi.divisor;

import java.util.Arrays;
import java.util.List;

import de.uni_augsburg.bazi.divisor.Output.Party;
import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Real;
import de.uni_augsburg.bazi.monoprop.MonopropMethod;
import de.uni_augsburg.bazi.monoprop.ShiftQueue;
import de.uni_augsburg.bazi.monoprop.ShiftQueue.NoShiftPossible;
import de.uni_augsburg.bazi.monoprop.Uniqueness;

public class PriorityQueueTest
{
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


	public static void main(String[] args) throws NoShiftPossible
	{
		Int allSeats = BMath.valueOf(4);
		// final Int[] seats = { BMath.ZERO, BMath.ZERO, BMath.ZERO };
		// final Int[] min = { BMath.valueOf(0), BMath.valueOf(0), BMath.valueOf(0) };
		// final Rational[] weights = { BMath.valueOf("1"), BMath.valueOf("1"), BMath.valueOf("1") };
		final RoundingFunction.Stationary r = new RoundingFunction.Stationary(BMath.HALF, null);

		List<Party> parties = Arrays.asList(
				new Party("A", BMath.valueOf("1"), BMath.valueOf(0), BMath.valueOf(2), BMath.ZERO, BMath.ZERO, BMath.ZERO, null),
				new Party("B", BMath.valueOf("1"), BMath.valueOf(0), BMath.valueOf(2), BMath.ZERO, BMath.ZERO, BMath.ZERO, null),
				new Party("C", BMath.valueOf("1"), BMath.valueOf(0), BMath.valueOf(10), BMath.ZERO, BMath.ZERO, BMath.ZERO, null)
				);
		List<Int> seats = Arrays.asList(BMath.ZERO, BMath.ZERO, BMath.ZERO);

		ShiftQueue q = new ShiftQueue(parties, seats, new Comp(r));
		q.shift(allSeats);
		List<Uniqueness> uniquenesses = q.getUniquenesses();

		System.out.println(seats);
		System.out.println(uniquenesses);
	}
}
