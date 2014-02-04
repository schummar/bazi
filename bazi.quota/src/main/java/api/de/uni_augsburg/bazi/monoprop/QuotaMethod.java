package de.uni_augsburg.bazi.monoprop;


public class QuotaMethod extends MonopropMethod<QuotaOutput>
{
	private final QuotaFunction quotaFunction;
	private final ResidualHandler residualHandler;

	public QuotaMethod(QuotaFunction quotaFunction, ResidualHandler residualHandler)
	{
		this.quotaFunction = quotaFunction;
		this.residualHandler = residualHandler;
	}

	@Override
	protected QuotaOutput calculateImpl(MonopropInput input)
	{
		return QuotaMethodAlgorithm.calculate(input, quotaFunction, residualHandler);
	}
}
