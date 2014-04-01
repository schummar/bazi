package de.uni_augsburg.bazi.quota;


import de.uni_augsburg.bazi.common.algorithm.Options;
import de.uni_augsburg.bazi.common.algorithm.VectorAlgorithm;
import de.uni_augsburg.bazi.common.algorithm.VectorInput;
import de.uni_augsburg.bazi.common.data.Data;

import java.util.Collections;
import java.util.List;

/** The quota algorithm. */
public class QuotaAlgorithm implements VectorAlgorithm
{
	private final QuotaFunction quotaFunction;
	private final ResidualHandler residualHandler;
	private final String name;

	/**
	 * @param quotaFunction the quota function.
	 * @param residualHandler the residual handler.
	 * @param name the display name of this algorithm.
	 */
	public QuotaAlgorithm(QuotaFunction quotaFunction, ResidualHandler residualHandler, String name)
	{
		this.quotaFunction = quotaFunction;
		this.residualHandler = residualHandler;
		this.name = name;
	}

	@Override public String name() { return name; }
	@Override public List<Object> getInputAttributes() { return Collections.emptyList(); }
	@Override public QuotaOutput applyUnfiltered(Data in, Options options)
	{
		return QuotaAlgorithmImpl.calculate(
			in.cast(VectorInput.class),
			quotaFunction,
			residualHandler,
			name
		);
	}
}
