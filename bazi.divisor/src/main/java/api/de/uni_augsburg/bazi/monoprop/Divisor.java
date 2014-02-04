package de.uni_augsburg.bazi.monoprop;

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

	public Real min() { return min; }
	public Real nice() { return nice; }
	public Real max() { return max; }
}
