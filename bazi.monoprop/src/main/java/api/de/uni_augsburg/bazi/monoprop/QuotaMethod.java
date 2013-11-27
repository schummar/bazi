package de.uni_augsburg.bazi.monoprop;

import com.google.common.collect.ImmutableList;

import de.uni_augsburg.bazi.common.Pair;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Rational;

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

	public static interface QuotaFunction
	{
		public Rational getQuota(Rational votes, Int seats);
	}

	public static interface ResidualHandler
	{
		public ImmutableList<Pair<Integer, Uniqueness>> getIncrementsAndUniqueness(ImmutableList<Pair<Int, Rational>> seatsAndRests, int residual);
	}
}
