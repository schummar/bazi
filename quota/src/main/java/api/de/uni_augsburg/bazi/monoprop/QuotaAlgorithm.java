package de.uni_augsburg.bazi.monoprop;


import de.uni_augsburg.bazi.common.Data;
import de.uni_augsburg.bazi.common.algorithm.VectorAlgorithm;
import de.uni_augsburg.bazi.common.algorithm.VectorInput;

import java.util.Arrays;
import java.util.List;

public class QuotaAlgorithm implements VectorAlgorithm<VectorInput, QuotaOutput>
{
	private final QuotaFunction quotaFunction;
	private final ResidualHandler residualHandler;

	public QuotaAlgorithm(QuotaFunction quotaFunction, ResidualHandler residualHandler)
	{
		this.quotaFunction = quotaFunction;
		this.residualHandler = residualHandler;
	}

	@Override public Class<VectorInput> getInputInterface() { return VectorInput.class; }
	@Override public List<Class<? extends Data>> getAllInputInterfaces() { return Arrays.asList(VectorInput.class); }
	@Override public QuotaOutput apply(VectorInput in) { return QuotaAlgorithmImpl.calculate(in, quotaFunction, residualHandler); }
}
