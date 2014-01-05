package de.uni_augsburg.bazi.monoprop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.monoprop.MonopropMethod.Input.Party;

public class ShiftQueue
{
	private final List<? extends Party> parties;
	private final List<Int> seats;
	private final Comp comp;

	private final List<Integer> increase, decrease;

	public ShiftQueue(List<? extends Party> parties, List<Int> seats, final Comp comp)
	{
		this.parties = parties;
		this.seats = seats;
		this.comp = comp;
		this.increase = new ArrayList<>();
		this.decrease = new ArrayList<>();
		for (int i = 0; i < parties.size(); i++)
		{
			increase.add(i);
			decrease.add(i);
		}
		update();
	}

	public void shift(Int n) throws NoShiftPossible
	{
		int sgn = n.sgn();
		if (sgn > 0)
		{
			for (Int i = BMath.ZERO; i.compareTo(n) < 0; i = i.add(1))
				increase();
		}
		else if (sgn < 0)
		{
			n = n.neg();
			for (Int i = BMath.ZERO; i.compareTo(n) < 0; i = i.add(1))
				decrease();
		}
	}

	public void increase() throws NoShiftPossible
	{
		int i = increase.get(0);
		Int s = seats.get(i).add(1);

		if (s.compareTo(parties.get(i).getMax()) > 0)
			throw new NoShiftPossible();

		seats.set(i, seats.get(i).add(1));
		update();
	}

	public int nextIncrease()
	{
		return increase.get(0);
	}

	public void decrease() throws NoShiftPossible
	{
		int i = decrease.get(0);
		Int s = seats.get(i).sub(1);

		if (s.compareTo(parties.get(i).getMin()) < 0)
			throw new NoShiftPossible();

		seats.set(i, seats.get(i).sub(1));
		update();
	}

	public int nextDecrease()
	{
		return decrease.get(0);
	}

	public List<Uniqueness> getUniquenesses()
	{
		List<Uniqueness> uniquenesses = new ArrayList<>();
		for (int i = 0; i < parties.size(); i++)
			uniquenesses.add(Uniqueness.UNIQUE);

		int lastIncrease = decrease.get(0);
		for (int i : increase)
			if (compare(parties.get(i), seats.get(i).add(1), parties.get(lastIncrease), seats.get(lastIncrease)) >= 0)
				uniquenesses.set(i, Uniqueness.CAN_BE_MORE);
			else
				break;

		int lastDecrease = increase.get(0);
		for (int i : decrease)
			if (compare(parties.get(i), seats.get(i), parties.get(lastDecrease), seats.get(lastDecrease).add(1)) <= 0)
				uniquenesses.set(i, Uniqueness.CAN_BE_LESS);
			else
				break;

		return uniquenesses;
	}

	private void update()
	{
		Collections.sort(increase, new Comparator<Integer>()
		{
			@Override public int compare(Integer i0, Integer i1)
			{
				int comp = -ShiftQueue.this.compare(parties.get(i0), seats.get(i0).add(1), parties.get(i1), seats.get(i1).add(1));
				if (comp == 0)
					comp = i0.compareTo(i1);
				return comp;
			}
		});
		Collections.sort(decrease, new Comparator<Integer>()
		{
			@Override public int compare(Integer i0, Integer i1)
			{
				int comp = ShiftQueue.this.compare(parties.get(i0), seats.get(i0).sub(1), parties.get(i1), seats.get(i1).sub(1));
				if (comp == 0)
					comp = -i0.compareTo(i1);
				return comp;
			}
		});
	}

	private int compare(Party p0, Int s0, Party p1, Int s1)
	{
		int compare = comp.compare(p0, s0, p1, s1);
		if (compare != 0)
			return compare;
		return bias(p0, s0) - bias(p1, s1);
	}

	private static int bias(MonopropMethod.Input.Party p, Int s)
	{
		if (s.sub(1).compareTo(p.getMin()) < 0)
			return 1;
		if (s.compareTo(p.getMax()) > 0)
			return -1;
		return 0;
	}

	public static interface Comp
	{
		public int compare(Party p0, Int s0, Party p1, Int s1);
	}

	public static class NoShiftPossible extends Exception
	{
		private static final long serialVersionUID = 1L;
	}
}
