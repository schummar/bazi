package de.uni_augsburg.bazi.monoprop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.monoprop.MonopropMethod.Input.Party;

public class InDecreaseQueue
{
	private final List<? extends Party> parties;
	private final Int[] seats;
	private final Comp comp;

	private final List<Integer> increase, decrease;

	public InDecreaseQueue(List<? extends Party> parties, Int[] seats, final Comp comp)
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

	public void increase(Int n) throws NoInDecreasePossible
	{
		for (Int i = BMath.ZERO; i.compareTo(n) < 0; i = i.add(1))
			increase();
	}

	public void increase() throws NoInDecreasePossible
	{
		int i = increase.get(0);
		Int s = seats[i].add(1);

		if (s.compareTo(parties.get(i).getMax()) > 0)
			throw new NoInDecreasePossible();

		seats[i] = seats[i].add(1);
		update();
	}

	public void decrease(Int n) throws NoInDecreasePossible
	{
		for (Int i = BMath.ZERO; i.compareTo(n) < 0; i = i.add(1))
			decrease();
	}

	public void decrease() throws NoInDecreasePossible
	{
		int i = decrease.get(0);
		Int s = seats[i].sub(1);

		if (s.compareTo(parties.get(i).getMin()) < 0)
			throw new NoInDecreasePossible();

		seats[i] = seats[i].sub(1);
		update();
	}

	public Uniqueness[] getUniquenesses()
	{
		Uniqueness[] uniquenesses = new Uniqueness[parties.size()];
		for (int i = 0; i < uniquenesses.length; i++)
			uniquenesses[i] = Uniqueness.UNIQUE;

		int lastIncrease = decrease.get(0);
		for (int i : increase)
			if (compare(parties.get(i), seats[i].add(1), parties.get(lastIncrease), seats[lastIncrease]) >= 0)
				uniquenesses[i] = Uniqueness.CAN_BE_MORE;
			else
				break;

		int lastDecrease = increase.get(0);
		for (int i : decrease)
			if (compare(parties.get(i), seats[i], parties.get(lastDecrease), seats[lastDecrease].add(1)) <= 0)
				uniquenesses[i] = Uniqueness.CAN_BE_LESS;
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
				int comp = -InDecreaseQueue.this.compare(parties.get(i0), seats[i0].add(1), parties.get(i1), seats[i1].add(1));
				if (comp == 0)
					comp = i0.compareTo(i1);
				return comp;
			}
		});
		Collections.sort(decrease, new Comparator<Integer>()
		{
			@Override public int compare(Integer i0, Integer i1)
			{
				int comp = InDecreaseQueue.this.compare(parties.get(i0), seats[i0].sub(1), parties.get(i1), seats[i1].sub(1));
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

	public static class NoInDecreasePossible extends Exception
	{
		private static final long serialVersionUID = 1L;
	}
}
