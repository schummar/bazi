package de.uni_augsburg.bazi.divisor;

import de.uni_augsburg.bazi.common.algorithm.Options;
import de.uni_augsburg.bazi.common.algorithm.VectorAlgorithm;
import de.uni_augsburg.bazi.common.algorithm.VectorInput;
import de.uni_augsburg.bazi.common.data.Data;

import java.util.Collections;
import java.util.List;


public class DivisorAlgorithm implements VectorAlgorithm<DivisorOutput>
{
	private final RoundingFunction roundingFunction;
	private final String name;

	public DivisorAlgorithm(RoundingFunction roundingFunction, String name)
	{
		this.roundingFunction = roundingFunction;
		this.name = name;
	}

	@Override public String name() { return name; }
	public RoundingFunction roundingFunction() { return roundingFunction; }

	@Override public List<Object> getInputAttributes() { return Collections.emptyList(); }
	@Override public DivisorOutput applyUnfiltered(Data in, Options options)
	{
		return DivisorAlgorithmImpl.calculate(
			in.cast(VectorInput.class),
			roundingFunction,
			name,
			options
		);
	}
}
