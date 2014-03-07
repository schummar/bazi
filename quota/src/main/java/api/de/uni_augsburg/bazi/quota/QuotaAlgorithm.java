package de.uni_augsburg.bazi.quota;


import de.uni_augsburg.bazi.common.algorithm.VectorAlgorithm;
import de.uni_augsburg.bazi.common.algorithm.VectorInput;

import java.util.Collections;
import java.util.List;

public class QuotaAlgorithm implements VectorAlgorithm
{
	private final QuotaFunction quotaFunction;
	private final ResidualHandler residualHandler;
	private final String name;

	public QuotaAlgorithm(QuotaFunction quotaFunction, ResidualHandler residualHandler, String name)
	{
		this.quotaFunction = quotaFunction;
		this.residualHandler = residualHandler;
		this.name = name;
	}

	@Override public List<Object> getInputAttributes() { return Collections.emptyList(); }
	@Override public QuotaOutput apply(VectorInput in) { return QuotaAlgorithmImpl.calculate(in, quotaFunction, residualHandler, name); }
}
