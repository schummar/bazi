package de.uni_augsburg.bazi.monoprop;

import de.uni_augsburg.bazi.monoprop.RoundingFunction.ExactRoundingFunction;
import de.uni_augsburg.bazi.common.algorithm.VectorPlugin;


public class DivisorAlgorithm extends VectorPlugin.VectorAlgorithm
{
	private final RoundingFunction roundingFunction;
	private final int minPrecision;

	public DivisorAlgorithm(RoundingFunction roundingFunction, int minPrecision)
	{
		this.roundingFunction = roundingFunction;
		this.minPrecision = minPrecision;
	}

	public DivisorAlgorithm(ExactRoundingFunction roundingFunction)
	{
		this.roundingFunction = roundingFunction;
		this.minPrecision = 0;
	}

	public RoundingFunction roundingFunction() { return roundingFunction; }
	public int minPrecision() { return minPrecision; }

	@Override
	protected DivisorOutput calculateImpl(MonopropInput input)
	{
		return DivisorMethodAlgorithm.calculate(input, roundingFunction, minPrecision);
	}
}
