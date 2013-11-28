package de.uni_augsburg.bazi.monoprop;

import java.util.Map;

import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Rational;

public class DivisorStaionaryMethod implements MonopropMethod
{
	public static final DivisorStaionaryMethod
			DIVSTD = new DivisorStaionaryMethod(BMath.rationalOf("0.5")),
			DIVDWN = new DivisorStaionaryMethod(BMath.intOf(1)),
			DIVUPW = new DivisorStaionaryMethod(BMath.intOf(0));

	public DivisorStaionaryMethod(Rational parameter)
	{}
	public DivisorStaionaryMethod(Rational parameter, Map<Int, Rational> special)
	{}

	@Override public Output calculate(Input input)
	{
		throw new RuntimeException("'Monoprop.calculate' not yet implemented");
	}
}
