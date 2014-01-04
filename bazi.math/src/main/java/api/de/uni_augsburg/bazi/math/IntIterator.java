package de.uni_augsburg.bazi.math;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class IntIterator implements Iterator<Int>
{
	private Int now;
	private final Int to, increment;

	public IntIterator(Int from, Int to)
	{
		this.now = from;
		this.to = to;
		this.increment = from.compareTo(to) <= 0 ? BMath.ONE : BMath.MINUS_ONE;
	}

	@Override public void remove()
	{
		throw new UnsupportedOperationException();
	}

	@Override public Int next()
	{
		if (!hasNext())
			throw new NoSuchElementException();
		Int temp = now;
		now = now.add(increment);
		return temp;
	}

	@Override public boolean hasNext()
	{
		return !now.equals(to);
	}
}
