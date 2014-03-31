package de.uni_augsburg.bazi.bmm_pow;

import de.uni_augsburg.bazi.common.algorithm.Algorithm;
import de.uni_augsburg.bazi.common.algorithm.Options;
import de.uni_augsburg.bazi.common.algorithm.VectorInput;
import de.uni_augsburg.bazi.common.data.Data;
import de.uni_augsburg.bazi.divisor.DivisorAlgorithm;
import de.uni_augsburg.bazi.math.Int;

import java.util.Collections;
import java.util.List;

/** The base+min..max(pow) algorithm. */
public class BMMPowAlgorithm implements Algorithm<BMMPowOutput>
{
	/** Constraints for the seats of each party. */
	public final Int base, min, max;

	/** The algorithm to calculate the actual apportionment with. */
	public final DivisorAlgorithm method;

	/**
	 * Consreuctor with initielizers.
	 * @param base the base seats for each party.
	 * @param min the min seats for each party.
	 * @param max the max seats for each party.
	 * @param method the algorithm to calculate the actual apportionment with.
	 */
	public BMMPowAlgorithm(Int base, Int min, Int max, DivisorAlgorithm method)
	{
		this.base = base;
		this.min = min;
		this.max = max;
		this.method = method;
	}

	@Override public String name()
	{
		return "bmmpow";
	}

	@Override public List<Object> getInputAttributes()
	{
		return Collections.emptyList();
	}


	@Override public BMMPowOutput applyUnfiltered(Data in, Options options)
	{
		return BMMPowAlgorithmImpl.calculate(in.cast(VectorInput.class), method, base, min, max, options);
	}
}
