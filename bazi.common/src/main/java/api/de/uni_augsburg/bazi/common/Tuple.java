package de.uni_augsburg.bazi.common;

public class Tuple<X, Y>
{
	public static <First, Second> Tuple<First, Second> of(First first, Second second)
	{
		return new Tuple<>(first, second);
	}

	private final X x;
	private final Y y;
	private transient int hash;

	public Tuple(X x, Y y)
	{
		this.x = x;
		this.y = y;
		this.hash = (x == null ? 0 : x.hashCode() * 31) + (y == null ? 0 : y.hashCode());
	}

	public X x() { return x; }

	public Y y() { return y; }

	@Override
	public boolean equals(Object that)
	{
		if (this == that) return true;
		if (that == null || !getClass().isInstance(that)) return false;
		@SuppressWarnings("unchecked")
		Tuple<X, Y> thatTuple = (Tuple<X, Y>) that;
		return x.equals(thatTuple.x) && y.equals(thatTuple.y);
	}

	@Override
	public int hashCode()
	{
		return hash;
	}
}
