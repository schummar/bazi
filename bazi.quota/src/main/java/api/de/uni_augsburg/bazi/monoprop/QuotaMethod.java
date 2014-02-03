package de.uni_augsburg.bazi.monoprop;


import java.util.List;

public class QuotaMethod extends MonopropMethod
{
	private final QuotaFunction quotaFunction;
	private final ResidualHandler residualHandler;

	public QuotaMethod(QuotaFunction quotaFunction, ResidualHandler residualHandler)
	{
		this.quotaFunction = quotaFunction;
		this.residualHandler = residualHandler;
	}

	@Override
	public QuotaOutput calculate(MonopropInput input)
	{
		QuotaOutput output = QuotaMethodAlgorithm.calculate(input, quotaFunction, residualHandler);
		return calculateDirectSeats(output);
	}

	@Override
	public List<? extends QuotaOutput> calculateAll(MonopropInput input)
	{
		return calculateApparenments(calculate(input));
	}
}
