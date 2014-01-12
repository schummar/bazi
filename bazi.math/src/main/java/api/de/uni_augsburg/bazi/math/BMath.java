package de.uni_augsburg.bazi.math;

import java.util.HashMap;
import java.util.Map;

import org.apfloat.Apint;
import org.apfloat.Aprational;
import org.apfloat.AprationalMath;

public class BMath
{
	public static final Int ZERO = new Int(0),
			ONE = new Int(1),
			MINUS_ONE = new Int(-1),
			TWO = new Int(2),
			INF = new Infinity(1),
			INFN = new Infinity(-1),
			NAN = new NaN();

	public static final Rational HALF = new Rational("0.5");

	public static final String INF_STRING = "oo", INFN_STRING = "-oo", NAN_STRING = "nan";

	private static final Map<Long, Int> LONG_TO_INT_CACHE = new HashMap<>();
	private static final Map<String, Rational> STRING_TO_RATIONAL_CACHE = new HashMap<>();


	public static Int valueOf(long l)
	{
		Int i = LONG_TO_INT_CACHE.get(l);
		if (i == null)
			LONG_TO_INT_CACHE.put(l, i = new Int(l));
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
				STRING_TO_RATIONAL_CACHE.put(s, q = fromApRational(parseString(s)));
			return q;
		}
	}

	static Rational fromApRational(Aprational apr)
	{
		if (apr instanceof Apint)
			return new Int((Apint) apr);
		return new Rational(apr);
	}

	static Aprational parseString(String s)
	{
		try
		{
			return new Apint(s);
		}
		catch (NumberFormatException e)
		{}
		try
		{
			return new Aprational(s);
		}
		catch (NumberFormatException e)
		{}
		try
		{
			int dot = s.indexOf(".");
			int scale = -(s.length() - dot - 1);
			String num = s.replaceAll("\\.", "");
			return AprationalMath.scale(new Apint(num), scale);
		}
		catch (NumberFormatException e)
		{}

		throw new NumberFormatException();
	}
}
