package de.uni_augsburg.bazi.common;

public class Pair<First, Second>
{
	public static <First, Second> Pair<First, Second> of(First first, Second second)
	{
		return new Pair<>(first, second);
	}

	private final First first;
	private final Second second;

	public Pair(First first, Second second)
	{
		this.first = first;
		this.second = second;
	}

	public First getFirst()
	{
		return first;
	}

	public Second getSecond()
	{
		return second;
	}
}
