package de.uni_augsburg.bazi.cl;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import de.uni_augsburg.bazi.common.Json;
import de.uni_augsburg.bazi.math.Int;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;

@Json.SerializeAsString
@Json.Deserialize(Interval.Deserialzer.class)
class Interval implements Iterable<Int>
{
	private final Int lo, hi;

	public Interval(Int lo, Int hi)
	{
		this.lo = lo;
		this.hi = hi;
	}

	public List<Int> values()
	{
		return lo.countTo(hi.add(1));
	}

	@Override
	public Iterator<Int> iterator()
	{
		return values().iterator();
	}

	@Override
	public String toString()
	{
		return lo.equals(hi) ? lo.toString() : String.format("%s..%s", lo, hi);
	}

	public static class Deserialzer implements JsonDeserializer<Interval>
	{
		@Override
		public Interval deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
		{
			String s = json.getAsString();
			s = s.replaceAll("\\s", "");
			try
			{
				String[] split = s.split("\\.\\.");
				Int lo = Int.valueOf(split[0]);
				Int hi = Int.valueOf(split[1]);
				return new Interval(lo, hi);
			}
			catch (Exception e) {}
			try
			{
				Int i = Int.valueOf(s);
				return new Interval(i, i);
			}
			catch (Exception e) {}
			return null;
		}
	}
}
