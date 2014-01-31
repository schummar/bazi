package de.uni_augsburg.bazi.monoprop;

import java.util.List;

public abstract class MonopropMethod<Output extends MonopropOutput>
{
	public final Output calculate(MonopropInput input)
	{
		Output output = calculateImpl(input);
		return DirectSeats.calculate(output);
	}

	public final List<Output> calculateAll(MonopropInput input)
	{
		Output output = calculateImpl(input);
		output = DirectSeats.calculate(output);
		return Apparentment.calculate(output, this);
	}

	abstract Output calculateImpl(MonopropInput input);
}