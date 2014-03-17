package de.uni_augsburg.bazi.bmm_pow;

import de.uni_augsburg.bazi.common.algorithm.Algorithm;
import de.uni_augsburg.bazi.common.algorithm.Options;
import de.uni_augsburg.bazi.common.algorithm.VectorInput;
import de.uni_augsburg.bazi.common.data.Data;
import de.uni_augsburg.bazi.divisor.DivisorAlgorithm;
import de.uni_augsburg.bazi.math.Int;

import java.util.Collections;
import java.util.List;

public class BMMPowAlgorithm extends Algorithm<BMMPowOutput>
{
	public final Int base, min, max;
	public final DivisorAlgorithm method;
	public BMMPowAlgorithm(Int base, Int min, Int max, DivisorAlgorithm method)
	{
		this.base = base;
		this.min = min;
		this.max = max;
		this.method = method;
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
