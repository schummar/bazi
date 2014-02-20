package de.uni_augsburg.bazi.monoprop;

import de.uni_augsburg.bazi.math.BMath;

public interface DivisorOutput extends VectorOutput
{
	Divisor divisor = new Divisor(BMath.INF, BMath.INF);

	public DivisorOutput() {}

	public DivisorOutput(MonopropInput input)
	{
		super(input);
	}

	public Divisor divisor()
	{
		return divisor;
	}
}
