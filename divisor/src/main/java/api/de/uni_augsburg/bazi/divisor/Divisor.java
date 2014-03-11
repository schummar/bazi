package de.uni_augsburg.bazi.divisor;

import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Interval;
import de.uni_augsburg.bazi.math.Real;

public class Divisor implements Interval
{
	private final Real min, nice, max;

	public Divisor(Real min, Real max)
	{
		this.min = min;
		this.max = max;
		this.nice = BMath.niceMidValue(this);
	}

	public Real min() { return min; }
	public Real nice() { return nice; }
	public Real max() { return max; }

	@Override public String toString()
	{
		return String.format("[%s,%s] -> %s]", min, max, nice);
	}
}
