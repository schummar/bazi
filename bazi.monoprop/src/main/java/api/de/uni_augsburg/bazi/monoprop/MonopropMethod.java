package de.uni_augsburg.bazi.monoprop;

import java.util.List;

public abstract class MonopropMethod
{
	protected  <Output extends MonopropOutput> Output calculateDirectSeats(Output output)
	{
		return DirectSeats.calculate(output);
	}

	protected <Output extends MonopropOutput> List<Output> calculateApparenments(Output output)
	{
		return Apparentment.calculate(output, this);
	}

	public abstract MonopropOutput calculate(MonopropInput input);
	public abstract List<? extends MonopropOutput> calculateAll(MonopropInput input);
}