package de.uni_augsburg.bazi.monoprop;

import de.uni_augsburg.bazi.monoprop.RoundingFunction.ExactRoundingFunction;


public class DivisorMethod extends MonopropMethod<DivisorOutput>
{
	private final RoundingFunction roundingFunction;
	private final int minPrecision;

	public DivisorMethod(RoundingFunction roundingFunction, int minPrecision)
	{
		this.roundingFunction = roundingFunction;
		this.minPrecision = minPrecision;
	}

	public DivisorMethod(ExactRoundingFunction roundingFunction)
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
