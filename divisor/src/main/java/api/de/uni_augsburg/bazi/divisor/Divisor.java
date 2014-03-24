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

@Converter(Divisor.Converter.class)
public class Divisor implements Interval
{
	private final Real min, nice, max;

	public Divisor(Real min, Real max)
	{
		this.min = min;
		this.max = max;
		this.nice = BMath.niceMidValue(this);
	}

	public Real min() { return min; }
	public Real nice() { return nice; }
	public Real max() { return max; }

	public Real minMultiplier() { return max.inv(); }
	public Real niceMultiplier() { return BMath.niceMidValue(Interval.of(minMultiplier(), maxMultiplier())); }
	public Real maxMultiplier() { return min.inv(); }

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
