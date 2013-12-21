package de.uni_augsburg.bazi.monoprop;

import java.util.Arrays;
import java.util.List;

import de.uni_augsburg.bazi.divisor.RoundingFunction;
import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Real;

public class PriorityQueueTest
{
	public static void main(String[] args)
	{
		Int allSeats = BMath.valueOf(4);
		// final Int[] seats = { BMath.ZERO, BMath.ZERO, BMath.ZERO };
		// final Int[] min = { BMath.valueOf(0), BMath.valueOf(0), BMath.valueOf(0) };
		// final Rational[] weights = { BMath.valueOf("1"), BMath.valueOf("1"), BMath.valueOf("1") };
		final RoundingFunction.Stationary rf = new RoundingFunction.Stationary(BMath.HALF, null);

		List<Party> parties = Arrays.asList(
				new Party("A", BMath.valueOf("1"), BMath.valueOf(0), BMath.valueOf(10), BMath.ZERO, null, null, null),
				new Party("B", BMath.valueOf("1"), BMath.valueOf(0), BMath.valueOf(10), BMath.ZERO, null, null, null),
				new Party("C", BMath.valueOf("1"), BMath.valueOf(0), BMath.valueOf(10), BMath.ZERO, null, null, null)
				);

		PriorityQueue.CompGen<Party, Real> compGen0 = new PriorityQueue.CompGen<Party, Real>()
		{
			@Override public Real getIncreseValue(Party arg)
			{
				return arg.getVotes().div(rf.getBorder(arg.getSeats()));
			}

			@Override public Real getDecreaseValue(Party arg)
			{
				return arg.getVotes().div(rf.getBorder(arg.getSeats().sub(1))).neg();
			}
		};

		PriorityQueue.CompGen<Party, Integer> compGen1 = new PriorityQueue.CompGen<Party, Integer>()
		{
			@Override public Integer getIncreseValue(Party arg)
			{
				if (arg.getSeats().compareTo(arg.getMin()) < 0)
					return 1;
				if (arg.getSeats().compareTo(arg.getMax()) >= 0)
					return -1;
				return 0;
			}

			@Override public Integer getDecreaseValue(Party arg)
			{
				if (arg.getSeats().compareTo(arg.getMin()) <= 0)
					return -1;
				if (arg.getSeats().compareTo(arg.getMax()) > 0)
					return 1;
				return 0;
			}
		};

		PriorityQueue<Party> pq = new PriorityQueue<Party>(parties, Arrays.asList(compGen0, compGen1));


		for (Int sum = BMath.ZERO; sum.compareTo(allSeats) < 0; sum = sum.add(1))
		{
			Party pOld = pq.nextIncrease();
			Party pNew = new Party(pOld.getName(), pOld.getVotes(), pOld.getSeats().add(1), pOld.getMin());

			int i = pq.nextIncrease();
			seats[i] = seats[i].add(1);
		}
		Uniqueness[] un = new Uniqueness[seats.length];
		for (int i = 0; i < un.length; i++)
			un[i] = pq.getUniqueness(i);

		System.out.println(Arrays.toString(seats));
		System.out.println(Arrays.toString(un));
	}
}
