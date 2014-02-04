package de.uni_augsburg.bazi.monoprop;

import de.uni_augsburg.bazi.monoprop.RoundingFunction.ExactRoundingFunction;

import java.util.List;


public class DivisorMethod extends MonopropMethod
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
	public DivisorOutput calculate(MonopropInput input)
	{
		DivisorOutput output = DivisorMethodAlgorithm.calculate(input, roundingFunction, minPrecision);
		return calculateDirectSeats(output);
	}

	@Override
	public List<? extends DivisorOutput> calculateAll(MonopropInput input)
	{
		return calculateApparenments(calculate(input));
	}
}
