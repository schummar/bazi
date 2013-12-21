package de.uni_augsburg.bazi.divisor;

import de.uni_augsburg.bazi.monoprop.MonopropMethod;


public class DivisorMethod implements MonopropMethod
{
	private final RoundingFunction roundingFunction;

	public DivisorMethod(RoundingFunction roundingFunction)
	{
		this.roundingFunction = roundingFunction;
	}

	@Override public Output calculate(Input input)
	{
		return DivisorMethodImpl.calculate(roundingFunction, input);
	}
}
