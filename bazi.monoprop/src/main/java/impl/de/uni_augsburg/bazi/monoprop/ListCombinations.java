package de.uni_augsburg.bazi.monoprop;

import java.util.function.Function;

public class ListCombinations
{
	public static <Out> Out calculate(MonopropMethod.Input input, Function<MonopropMethod.Input, Out> method)
	{
		return method.apply(input);
	}
}
