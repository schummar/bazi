package de.uni_augsburg.bazi.divisor;

import de.uni_augsburg.bazi.common.algorithm.VectorAlgorithm;
import de.uni_augsburg.bazi.common.algorithm.VectorInput;
import de.uni_augsburg.bazi.common.data.Data;
import de.uni_augsburg.bazi.divisor.RoundingFunction.ExactRoundingFunction;

import java.util.Collections;
import java.util.List;


public class DivisorAlgorithm extends VectorAlgorithm<DivisorOutput>
{
	private final RoundingFunction roundingFunction;
	private final int minPrecision;
	private final String name;

	public DivisorAlgorithm(RoundingFunction roundingFunction, int minPrecision, String name)
	{
		this.roundingFunction = roundingFunction;
		this.minPrecision = minPrecision;
		this.name = name;
	}

	public DivisorAlgorithm(ExactRoundingFunction roundingFunction, String name)
	{
		this.roundingFunction = roundingFunction;
		this.minPrecision = 0;
		this.name = name;
	}

	public RoundingFunction roundingFunction() { return roundingFunction; }
	public int minPrecision() { return minPrecision; }

	@Override public List<Object> getInputAttributes() { return Collections.emptyList(); }
	@Override public DivisorOutput applyUnfiltered(Data in)
	{
		return DivisorAlgorithmImpl.calculate(
			in.cast(VectorInput.class),
			roundingFunction,
			minPrecision,
			name
		);
	}
}
