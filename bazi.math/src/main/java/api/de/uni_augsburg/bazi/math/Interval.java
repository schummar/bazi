package de.uni_augsburg.bazi.math;

import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.UnaryOperator;

public class Interval<T> implements Set<T>
{
	public static Set<Integer> zeroTo(int border)
	{
		return new Interval<>(0, border, i -> i + 1);
	}


	private final T from, to;
	private final UnaryOperator<T> incrementer;
	public Interval(T from, T to, UnaryOperator<T> incrementer)
	{
		this.from = from;
		this.to = to;
		this.incrementer = incrementer;
	}

	private int size = -1;
	@Override
	public int size()
	{
		if (size >= 0) return size;
		size = 0;
		for (T t : this) size++;
		return size;
	}

	@Override
	public boolean isEmpty()
	{
		return from.equals(to);
	}

	@Override
	public boolean contains(Object o)
	{
		for (T t : this)
			if (t.equals(o)) return true;
		return false;
	}

	@Override
	public Iterator<T> iterator()
	{
		return new Iterator<T>()
		{
			private T next = from;
			@Override
			public boolean hasNext()
			{
				return !next.equals(to);
			}
			@Override
			public T next()
			{
				if (!hasNext()) throw new NoSuchElementException();
				T cur = next;
				next = incrementer.apply(next);
				return cur;
			}
		};
	}

	@Override
	public Object[] toArray()
	{
		return Lists.newArrayList(iterator()).toArray();
	}

	@Override
	public <T1> T1[] toArray(T1[] a)
	{
		return Lists.newArrayList(iterator()).toArray(a);
	}

	@Override
	public boolean add(T t)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean remove(Object o)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean containsAll(Collection<?> c)
	{
		for (Object o : c)
			if (!contains(o)) return false;
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends T> c)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(Collection<?> c)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(Collection<?> c)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear()
	{
		throw new UnsupportedOperationException();
	}
}
