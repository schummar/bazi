package de.uni_augsburg.bazi.monoprop;

public abstract class MonopropMethod<Output extends MonopropOutput>
{
	public Output calculate(MonopropInput input)
	{
		return DirectSeats.apply(calculateImpl(input));
	}

	public Output calculateDeep(MonopropInput input, MonopropMethod<?>... subMethods)
	{
		if (subMethods.length == 0) subMethods = new MonopropMethod[]{this};
		return Apparentment.apply(calculate(input), subMethods);
	}

	protected abstract Output calculateImpl(MonopropInput input);
}