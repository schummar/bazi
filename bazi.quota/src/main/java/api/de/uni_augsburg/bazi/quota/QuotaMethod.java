package de.uni_augsburg.bazi.quota;

import java.util.List;

import de.uni_augsburg.bazi.math.Int;
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

	public static class Output extends MonopropMethod.Output
	{
		private final Rational quota;

		public Output(Int seats, List<? extends Party> parties, Rational quota)
		{
			super(seats, parties);
			this.quota = quota;
		}
		public Rational getQuota()
		{
			return quota;
		}
	}
}
