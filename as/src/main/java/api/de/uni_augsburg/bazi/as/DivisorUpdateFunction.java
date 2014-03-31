package de.uni_augsburg.bazi.as;

import de.uni_augsburg.bazi.common.format.Converter;
import de.uni_augsburg.bazi.common.format.ObjectConverter;
import de.uni_augsburg.bazi.divisor.Divisor;
import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Real;


/** The way the divisors are updates between iterations. */
@Converter(DivisorUpdateFunction.Converter.class)
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


	public static class Converter implements ObjectConverter<DivisorUpdateFunction>
	{
		@Override public Object serialize(DivisorUpdateFunction value)
		{
			return "mid";
		}
		@Override public DivisorUpdateFunction deserialize(Object value)
		{
			return MIDPOINT;
		}
	}
}
