package de.uni_augsburg.bazi.divisor;

import de.uni_augsburg.bazi.common.format.Converter;
import de.uni_augsburg.bazi.common.format.ObjectConverter;
import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Interval;
import de.uni_augsburg.bazi.math.Real;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/** An interval of Real numbers with possibly a nicer value that is element of that interval. */
@Converter(Divisor.Converter.class)
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


	public static class Converter implements ObjectConverter<Divisor>
	{
		private static final Logger LOGGER = LoggerFactory.getLogger(Converter.class);

		@Override public Object serialize(Divisor value)
		{
			Map<String, String> map = new HashMap<>();
			try
			{
				map.put(Divisor.class.getMethod("min").getName(), value.min().toString());
				map.put(Divisor.class.getMethod("nice").getName(), value.nice().toString());
				map.put(Divisor.class.getMethod("max").getName(), value.max().toString());
			}
			catch (Exception e)
			{
				LOGGER.warn(e.getMessage());
			}

			return map;
		}
	}
}
