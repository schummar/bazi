package de.uni_augsburg.bazi.divisor;

import de.schummar.castable.Castable;
import de.schummar.castable.CastableObject;
import de.schummar.castable.Convert;
import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Interval;
import de.uni_augsburg.bazi.math.Real;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
		private static final Logger LOGGER = LoggerFactory.getLogger(Converter.class);
		@Override public Divisor apply(Castable castable)
		{
			Real min = null, nice = null, max = null;
			CastableObject co = castable.asCastableObject();
			try
			{
				min = (Real) co.getProperty(Divisor.class.getMethod("min")).getValue();
				nice = (Real) co.getProperty(Divisor.class.getMethod("nice")).getValue();
				max = (Real) co.getProperty(Divisor.class.getMethod("max")).getValue();
			}
			catch (Exception e)
			{
				LOGGER.warn(e.getMessage());
			}
			return new Divisor(min, nice, max);
		}
		@Override public Castable applyInverse(Divisor divisor)
		{
			CastableObject co = new CastableObject();
			try
			{
				co.getProperty(Divisor.class.getMethod("min")).setValue(divisor.min());
				co.getProperty(Divisor.class.getMethod("nice")).setValue(divisor.nice());
				co.getProperty(Divisor.class.getMethod("max")).setValue(divisor.max());
			}
			catch (Exception e)
			{
				LOGGER.warn(e.getMessage());
			}
			return co;
		}
	}
}
