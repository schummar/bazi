package de.uni_augsburg.bazi.quota;

import de.uni_augsburg.bazi.math.Rational;
import de.uni_augsburg.bazi.monoprop.MonopropMethod;


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

	public static interface Output extends MonopropMethod.Output
	{
		public Rational getQuota();
	}
}
