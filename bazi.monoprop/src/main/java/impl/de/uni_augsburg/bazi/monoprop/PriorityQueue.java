package de.uni_augsburg.bazi.monoprop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.uni_augsburg.bazi.math.Real;

public abstract class PriorityQueue
{
	private final List<Integer> increase, decrease;

	public PriorityQueue(int size)
	{
		this.increase = new ArrayList<>();
		this.decrease = new ArrayList<>();

		for (int i = 0; i < size; i++)
		{
			this.increase.add(i);
			this.decrease.add(i);
		}
	}

	private final Comparator<Integer> increaseComparator = new Comparator<Integer>()
	{
		@Override public int compare(Integer o1, Integer o2)
		{
			return -PriorityQueue.compare(increaseValue(o1), increasePreference(o1), increaseValue(o2), increasePreference(o2));
		}
	};
	private final Comparator<Integer> decreaseComparator = new Comparator<Integer>()
	{
		@Override public int compare(Integer o1, Integer o2)
		{
			return -PriorityQueue.compare(decreaseValue(o1).neg(), -decreasePreference(o1), decreaseValue(o2).neg(), -decreasePreference(o2));
		}
	};

	private static int compare(Real value1, int advantage1, Real value2, int advantage2)
	{
		int comp = value1.compareTo(value2);
		if (comp != 0)
			return comp;
		return advantage1 - advantage2;
	}

	public int nextIncrease()
	{
		Collections.sort(increase, increaseComparator);
		return increase.get(0);
	}

	public int nextDecrease()
	{
		Collections.sort(decrease, decreaseComparator);
		return decrease.get(0);
	}

	public Uniqueness getUniqueness(int i)
	{
		if (canBeMore(i))
			return Uniqueness.CAN_BE_MORE;
		if (canBeLess(i))
			return Uniqueness.CAN_BE_LESS;
		return Uniqueness.UNIQUE;
	}

	public boolean canBeMore(int i)
	{
		Collections.sort(increase, increaseComparator);
		Collections.sort(decrease, decreaseComparator);
		int nextDecerase = nextDecrease();
		return compare(decreaseValue(nextDecerase), -decreasePreference(nextDecerase), increaseValue(i), increasePreference(i)) <= 0;
	}
	public boolean canBeLess(int i)
	{
		Collections.sort(increase, increaseComparator);
		Collections.sort(decrease, decreaseComparator);
		int nextIncrease = nextIncrease();
		return compare(increaseValue(nextIncrease).neg(), -increasePreference(nextIncrease), decreaseValue(i).neg(), decreasePreference(i)) <= 0;
	}


	public abstract Real increaseValue(int i);
	public abstract Real decreaseValue(int i);
	public abstract int increasePreference(int i);
	public abstract int decreasePreference(int i);
}
