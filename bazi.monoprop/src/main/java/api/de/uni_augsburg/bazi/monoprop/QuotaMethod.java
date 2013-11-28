package de.uni_augsburg.bazi.monoprop;


public class QuotaMethod implements MonopropMethod
{
	private final QuotaFunction quotaFunction;
	private final ResidualHandler residualHandler;

	public QuotaMethod(QuotaFunction quotaFunction, ResidualHandler residualHandler)
	{
		this.quotaFunction = quotaFunction;
		this.residualHandler = residualHandler;
	}

	@Override public Output calculate(Input input)
	{
		return QuotaMethodImpl.calculate(quotaFunction, residualHandler, input);
	}
}
