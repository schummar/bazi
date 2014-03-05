package de.uni_augsburg.bazi.divisor;

import de.uni_augsburg.bazi.common.algorithm.VectorOutput;

public interface DivisorOutput extends VectorOutput
{
	public Divisor divisor();
	public void divisor(Divisor divisor);
}
