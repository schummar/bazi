package de.uni_augsburg.bazi.as;

import de.schummar.castable.Castable;
import de.schummar.castable.CastableString;
import de.schummar.castable.Convert;
import de.uni_augsburg.bazi.divisor.Divisor;
import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Real;


/** The way the divisors are updates between iterations. */
@Convert(DivisorUpdateFunction.Converter.class)
public interface DivisorUpdateFunction
{
	/**
	 * Calculates the new divisor.
	 * @param divisor the divisor interval.
	 * @param fault the current fault.
	 * @return the new divisor as a Real.
	 */
	public Real apply(Divisor divisor, Int fault);

	public static DivisorUpdateFunction

		/** Use a nice value out of the interval. */
		MIDPOINT = (d, f) -> d.nice(),

	/** Use a random value out of the interval. */
	RANDOM = (d, f) -> d.max().equals(BMath.INF)
		? d.min().add(d.nice().sub(d.min()).mul(BMath.random()))
		: d.min().add(d.max().sub(d.min()).mul(BMath.random())),

	/**
	 * Use an extreme value out of the interval.
	 * If the row's fault is positive use the maximum.
	 * If the row's fault is negative use the minimum.
	 * Else use a nice value out of the interval.
	 */
	EXTREME = (d, f) -> {
		if (f.compareTo(0) > 0 && !d.max().equals(BMath.INF))
			return d.max();
		if (f.compareTo(0) < 0)
			return d.min();
		return d.nice();
	};


	public static class Converter implements de.schummar.castable.Converter<DivisorUpdateFunction>
	{
		@Override public DivisorUpdateFunction unpack(Castable castable)
		{
			return MIDPOINT;
		}
		@Override public Castable pack(DivisorUpdateFunction divisorUpdateFunction)
		{
			return new CastableString("mid");
		}
	}
}
