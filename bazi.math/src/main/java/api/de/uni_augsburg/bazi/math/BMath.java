package de.uni_augsburg.bazi.math;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.stream.JsonReader;

import de.uni_augsburg.bazi.common.Json;

public class BMath
{
	private static final Map<Long, Int> LONG_TO_INT_CACHE = new HashMap<>();
	private static final Map<String, Int> STRING_TO_INT_CACHE = new HashMap<>();
	private static final Map<String, Rational> STRING_TO_RATIONAL_CACHE = new HashMap<>();


	public static final Int ZERO = new BInt(0), ONE = new BInt(1), MINUS_ONE = new BInt(-1);
	public static final Int INF = new Infinity(1), INFN = new Infinity(-1), NAN = new NaN();
	public static final int DEFAULT_PRECISION = 16;
	public static final Rational HALF = valueOf("0.5");

	public static final String INF_STRING = "oo", INFN_STRING = "-oo", NAN_STRING = "nan";

	public static Int valueOf(long l)
	{
		Int i = LONG_TO_INT_CACHE.get(l);
		if (i == null)
			LONG_TO_INT_CACHE.put(l, i = new BInt(l));
		return i;
	}

	public static Int intValueOf(String s)
	{
		Int i = STRING_TO_INT_CACHE.get(s);
		if (i == null)
			STRING_TO_INT_CACHE.put(s, i = valueOf(s).floor());
		return i;
	}

	public static Rational valueOf(String s)
	{
		switch (s.toLowerCase())
		{
		case INF_STRING:
			return INF;
		case INFN_STRING:
			return INFN;
		case NAN_STRING:
			return NAN;
		default:
			Rational q = STRING_TO_RATIONAL_CACHE.get(s);
			if (q == null)
				STRING_TO_RATIONAL_CACHE.put(s, q = new BRational(new BigRational(s)));
			return q;
		}
	}

	public static Real min(Real... rs)
	{
		Real min = INF;
		for (Real r : rs)
			min = min.min(r);
		return min;
	}

	public static Real max(Real... rs)
	{
		Real max = INF;
		for (Real r : rs)
			max = max.max(r);
		return max;
	}

	public static Rational min(Rational... qs)
	{
		Rational min = INF;
		for (Rational q : qs)
			min = min.min(q);
		return min;
	}

	public static Rational max(Rational... qs)
	{
		Rational max = INF;
		for (Rational q : qs)
			max = max.max(q);
		return max;
	}

	public static Int min(Int... is)
	{
		Int min = INF;
		for (Int i : is)
			min = min.min(i);
		return min;
	}

	public static Int max(Int... is)
	{
		Int max = INF;
		for (Int i : is)
			max = max.max(i);
		return max;
	}

	public static class IntDeserializer implements Json.Deserializer<Int>
	{
		@Override public Int deserialize(JsonReader in) throws IOException
		{
			return intValueOf(in.nextString());
		}
	}

	public static class RationalDeserializer implements Json.Deserializer<Rational>
	{
		@Override public Rational deserialize(JsonReader in) throws IOException
		{
			return valueOf(in.nextString());
		}
	}

	public static class RealDeserializer implements Json.Deserializer<Real>
	{
		@Override public Real deserialize(JsonReader in) throws IOException
		{
			return valueOf(in.nextString());
		}
	}
}
