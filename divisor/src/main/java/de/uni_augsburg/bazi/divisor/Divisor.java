package de.uni_augsburg.bazi.divisor;

import de.schummar.castable.Castable;
import de.schummar.castable.CastableObject;
import de.schummar.castable.Convert;
import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Interval;
import de.uni_augsburg.bazi.math.Real;

/** An interval of Real numbers with possibly a nicer value that is element of that interval. */
@Convert(Divisor.Converter.class)
public class Divisor implements Interval
{
	private final Real min, nice, max;

	/**
	 * @param min the lower endpoint.
	 * @param max the upper endpoint.
	 */
	public Divisor(Real min, Real max)
	{
		this.min = min;
		this.max = max;
		this.nice = BMath.niceMidValue(this);
	}

	public Divisor(Real min, Real nice, Real max)
	{
		this.min = min;
		this.nice = nice;
		this.max = max;
	}

	/**
	 * The lower endpoint.
	 * @return the lower endpoint.
	 */
	public Real min()
	{
		return min;
	}

	/**
	 * A 'nice' value in the interval.
	 * More precisely a number with the least decimal places that still lies within the interval.
	 * @return a 'nice' value in the interval.
	 */
	public Real nice()
	{
		return nice;
	}

	/**
	 * The upper endpoint.
	 * @return the upper endpoint.
	 */
	public Real max()
	{
		return max;
	}


	/**
	 * The lower endpoint of the multiplier interval (which is the inverse divisor interval).
	 * @return the lower endpoint of the multiplier interval (which is the inverse divisor interval).
	 */
	public Real minMultiplier()
	{
		return max.inv();
	}

	/**
	 * A 'nice' value in the multiplier interval.
	 * More precisely a number with the least decimal places that still lies within the interval.
	 * @return a 'nice' value in the multiplier interval.
	 */
	public Real niceMultiplier()
	{
		return BMath.niceMidValue(Interval.of(minMultiplier(), maxMultiplier()));
	}

	/**
	 * The upper endpoint of the multiplier interval (which is the inverse divisor interval).
	 * @return the upper endpoint of the multiplier interval (which is the inverse divisor interval).
	 */
	public Real maxMultiplier()
	{
		return min.inv();
	}


	@Override public String toString()
	{
		return String.format("[%s,%s] -> %s]", min, max, nice);
	}


	public static class Converter implements de.schummar.castable.Converter<Divisor>
	{
		private static final de.schummar.castable.Converter<Real> REAL_CONVERTER = new Real.Converter();
		@Override public Divisor unpack(Castable castable)
		{
			CastableObject co = castable.asCastableObject();
			Real min = co.getProperty("min", REAL_CONVERTER, "-oo").getValue();
			Real max = co.getProperty("max", REAL_CONVERTER, "oo").getValue();
			Real nice = co.getProperty("nice", REAL_CONVERTER, "0").getValue();
			if (!Interval.of(min, max).contains(nice)) nice = null;
			return nice == null ? new Divisor(min, max) : new Divisor(min, nice, max);
		}
		@Override public Castable pack(Divisor divisor)
		{
			CastableObject co = new CastableObject();
			if (divisor != null)
			{
				co.getProperty("min", REAL_CONVERTER).setValue(divisor.min());
				co.getProperty("max", REAL_CONVERTER).setValue(divisor.max());
				co.getProperty("nice", REAL_CONVERTER).setValue(divisor.nice);
			}
			return co;
		}
	}
}
