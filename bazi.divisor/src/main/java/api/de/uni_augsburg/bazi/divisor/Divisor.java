package de.uni_augsburg.bazi.divisor;

import de.uni_augsburg.bazi.math.Real;

public class Divisor
{
	private final Real min, nice, max;

	public Divisor(Real min, Real max)
	{
		this.min = min;
		this.max = max;
		this.nice = min.add(max).div(2);
	}

	public Real getMin()
	{
		return min;
	}

	public Real getNice()
	{
		return nice;
	}

	public Real getMax()
	{
		return max;
	}
}
