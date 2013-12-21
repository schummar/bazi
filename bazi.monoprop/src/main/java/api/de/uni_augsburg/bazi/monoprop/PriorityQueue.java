package de.uni_augsburg.bazi.monoprop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PriorityQueue<A>
{
	public static interface CompGen<A, C extends Comparable<C>>
	{
		public C getIncreseValue(A arg);
		public C getDecreaseValue(A arg);
	}

	private static enum CompType
	{
		INCREASE
		{
			@Override public <A, C extends Comparable<C>> C get(CompGen<A, C> compGen, A arg)
			{
				return compGen.getIncreseValue(arg);
			}
		},
		DECREASE
		{
			@Override public <A, C extends Comparable<C>> C get(CompGen<A, C> compGen, A arg)
			{
				return compGen.getDecreaseValue(arg);
			}
		};
		public abstract <A, C extends Comparable<C>> C get(CompGen<A, C> compGen, A arg);
	}

	private final List<A> data;
	private final List<CompGen<A, ? extends Comparable<?>>> compGens;
	private final List<Integer> increase, decrease;

	public PriorityQueue(List<A> data, List<CompGen<A, ? extends Comparable<?>>> compGens)
	{
		this.data = data;
		this.compGens = compGens;
		this.increase = new ArrayList<>();
		this.decrease = new ArrayList<>();

		for (int i = 0; i < data.size(); i++)
		{
			this.increase.add(i);
			this.decrease.add(i);
		}
	}

	private final Comparator<Integer> increaseComparator = new Comparator<Integer>()
	{
		@Override public int compare(Integer o1, Integer o2)
		{
			return -PriorityQueue.this.compare(data.get(o1), CompType.INCREASE, data.get(o2), CompType.INCREASE);
		}
	};
	private final Comparator<Integer> decreaseComparator = new Comparator<Integer>()
	{
		@Override public int compare(Integer o1, Integer o2)
		{
			return -PriorityQueue.this.compare(data.get(o1), CompType.DECREASE, data.get(o2), CompType.DECREASE);
		}
	};

	private int compare(A arg0, CompType compType0, A arg1, CompType compType1)
	{
		for (CompGen<A, ?> compGen : compGens)
		{
			int comp = compare(arg0, compType0, arg1, compType1, compGen);
			if (comp != 0)
				return comp;
		}
		return 0;
	}

	private static <A, C extends Comparable<C>> int compare(A arg0, CompType compType0, A arg1, CompType compType1, CompGen<A, C> compGen)
	{
		return compType0.get(compGen, arg0).compareTo(compType1.get(compGen, arg1));
	}

	public A nextIncrease()
	{
		Collections.sort(increase, increaseComparator);
		return data.get(increase.get(0));
	}

	public A nextDecrease()
	{
		Collections.sort(decrease, decreaseComparator);
		return data.get(decrease.get(0));
	}

	public Uniqueness getUniqueness(A arg)
	{
		if (canBeMore(arg))
			return Uniqueness.CAN_BE_MORE;
		if (canBeLess(arg))
			return Uniqueness.CAN_BE_LESS;
		return Uniqueness.UNIQUE;
	}

	public boolean canBeMore(A arg)
	{
		Collections.sort(increase, increaseComparator);
		Collections.sort(decrease, decreaseComparator);
		A nextDecerase = nextDecrease();
		return compare(nextDecerase, CompType.DECREASE, arg, CompType.INCREASE) <= 0;
	}

	public boolean canBeLess(A arg)
	{
		Collections.sort(increase, increaseComparator);
		Collections.sort(decrease, decreaseComparator);
		A nextIncrease = nextIncrease();
		return compare(nextIncrease, CompType.INCREASE, arg, CompType.DECREASE) <= 0;
	}
}
