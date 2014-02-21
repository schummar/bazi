package de.uni_augsburg.bazi.monoprop;

import de.uni_augsburg.bazi.common.Data;
import de.uni_augsburg.bazi.common.algorithm.VectorAlgorithm;
import de.uni_augsburg.bazi.common.algorithm.VectorInput;
import de.uni_augsburg.bazi.monoprop.RoundingFunction.ExactRoundingFunction;

import java.util.Arrays;
import java.util.List;


public class DivisorAlgorithm implements VectorAlgorithm<VectorInput, DivisorOutput>
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


	@Override public Class<VectorInput> getInputInterface() { return VectorInput.class; }
	@Override public List<Class<? extends Data>> getAllInputInterfaces() { return Arrays.asList(VectorInput.class); }
	@Override public DivisorOutput apply(VectorInput in) { return DivisorAlgorithmImpl.calculate(in, roundingFunction, minPrecision); }
}
