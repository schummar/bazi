package de.uni_augsburg.bazi.divisor;

import de.uni_augsburg.bazi.common.algorithm.Options;
import de.uni_augsburg.bazi.common.algorithm.VectorAlgorithm;
import de.uni_augsburg.bazi.common.algorithm.VectorInput;
import de.uni_augsburg.bazi.common.data.Data;

import java.util.Collections;
import java.util.List;

/** A divisor algorithm. */
public class DivisorAlgorithm implements VectorAlgorithm<DivisorOutput>
{
	private final RoundingFunction roundingFunction;
	private final String name;

	/**
	 * @param roundingFunction the rounding function this algorithm uses.
	 * @param name the display name of this algorithm.
	 */
	public DivisorAlgorithm(RoundingFunction roundingFunction, String name)
	{
		this.roundingFunction = roundingFunction;
		this.name = name;
	}

	@Override public String name() { return name; }

	/**
	 * The rounding function this algorithm uses.
	 * @return the rounding function this algorithm uses.
	 */
	public RoundingFunction roundingFunction()
	{
		return roundingFunction;
	}

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
