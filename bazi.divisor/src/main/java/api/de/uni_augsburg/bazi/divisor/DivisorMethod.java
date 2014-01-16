package de.uni_augsburg.bazi.divisor;

import de.uni_augsburg.bazi.divisor.RoundingFunction.ExactRoundingFunction;
import de.uni_augsburg.bazi.monoprop.MonopropMethod;


public class DivisorMethod implements MonopropMethod
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

	@Override public Output calculate(Input input)
	{
		return DivisorMethodImpl.calculate(roundingFunction, input, minPrecision);
	}

	public static interface Output extends MonopropMethod.Output
	{
		public Divisor getDivisor();
	}
}
