package de.uni_augsburg.bazi.divisor;

import de.uni_augsburg.bazi.common.algorithm.VectorOutput;

/** Output of an DivisorAlgorithm. */
public interface DivisorOutput extends VectorOutput
{
	/**
	 * The divisor.
	 * @return the divisor.
	 */
	public Divisor divisor();

	/**
	 * The divisor.
	 * @param divisor the divisor.
	 */
	public void divisor(Divisor divisor);
}
