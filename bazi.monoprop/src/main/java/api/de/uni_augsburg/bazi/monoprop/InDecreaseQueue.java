package de.uni_augsburg.bazi.monoprop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.monoprop.MonopropMethod.Input.Party;

public class InDecreaseQueue
{
	private final List<? extends Party> parties;
	private final Int[] seats;
	private final Comp comp;

	private final List<Integer> increase, decrease;

	public InDecreaseQueue(List<? extends Party> parties, Int[] seats, Comp comp)
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
		Collections.sort(increase, increaseComp);
		Collections.sort(decrease, decreaseComp);
	}

	public void increase(Int n) throws NoInDecreasePossible
	{
		for (Iterator<Int> i = n.iterator(); i.hasNext(); i.next())
			increase();
	}
	public void increase() throws NoInDecreasePossible
	{
		int i = increase.remove(0);
		Int s = seats[i].add(1);

		if (!comp.canHaveSeats(parties.get(i), s))
			throw new NoInDecreasePossible();

		seats[i] = seats[i].add(1);

		for (int j = increase.size(); j >= 0; j--)
			if (j == 0 || increaseComp.compare(i, increase.get(j - 1)) <= 0)
			{
				increase.add(j, i);
			}
	}

	public void decrease(int n) throws NoInDecreasePossible
	{
		for (int i = 0; i < n; i++)
			decrease();
	}

	public void decrease() throws NoInDecreasePossible
	{
		int i = decrease.remove(0);
		Int s = seats[i].sub(1);

		if (!comp.canHaveSeats(parties.get(i), s))
			throw new NoInDecreasePossible();

		seats[i] = seats[i].sub(1);

		for (int j = decrease.size(); j >= 0; j--)
			if (j == 0 || decreaseComp.compare(i, decrease.get(j - 1)) <= 0)
			{
				decrease.add(j, i);
			}
	}

	public Uniqueness[] getUniquenesses()
	{
		Uniqueness[] uniquenesses = new Uniqueness[parties.size()];
		for (int i = 0; i < uniquenesses.length; i++)
			uniquenesses[i] = Uniqueness.UNIQUE;

		int lastIncrease = decrease.get(0);
		for (int i : increase)
			if (comp.compare(parties.get(i), seats[i].add(1), parties.get(lastIncrease), seats[lastIncrease]) >= 0)
				uniquenesses[i] = Uniqueness.CAN_BE_MORE;
			else
				break;

		int lastDecrease = increase.get(0);
		for (int i : decrease)
			if (comp.compare(parties.get(i), seats[i].add(1), parties.get(lastDecrease), seats[lastDecrease]) <= 0)
				uniquenesses[i] = Uniqueness.CAN_BE_LESS;
			else
				break;

		return uniquenesses;
	}


	private final Comparator<Integer> increaseComp = new Comparator<Integer>()
	{
		@Override public int compare(Integer i0, Integer i1)
		{
			return comp.compare(parties.get(i0), seats[i0].add(1), parties.get(i1), seats[i1].add(1));
		}
	};

	private final Comparator<Integer> decreaseComp = new Comparator<Integer>()
	{
		@Override public int compare(Integer i0, Integer i1)
		{
			return -comp.compare(parties.get(i0), seats[i0].sub(1), parties.get(i1), seats[i1].sub(1));
		}
	};

	public static interface Comp
	{
		public int compare(Party p0, Int s0, Party p1, Int s1);
		public boolean canHaveSeats(Party p, Int s);
	}

	public static class NoInDecreasePossible extends Exception
	{
		private static final long serialVersionUID = 1L;
	}
}
