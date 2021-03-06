package de.uni_augsburg.bazi.math;

import org.apfloat.Apfloat;
import org.apfloat.Apint;
import org.apfloat.Aprational;
import org.apfloat.AprationalMath;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class BMath
{
	public static final Int ZERO = new Int(0),
		ONE = new Int(1),
		MINUS_ONE = new Int(-1),
		TWO = new Int(2),
		MINUS_TWO = new Int(-2),
		TEN = new Int(10),
		MINUS_TEN = new Int(-10),
		INF = new Infinity(1),
		INFN = new Infinity(-1),
		NAN = new NaN();

	public static final Rational HALF = new Rational("0.5");

	public static final String INF_STRING = "oo", INFN_STRING = "-oo", NAN_STRING = "NaN", NAN_STRING_LOW = "nan";

	private static final Map<Long, Int> LONG_TO_INT_CACHE = new HashMap<>();
	private static final Map<String, Rational> STRING_TO_RATIONAL_CACHE = new HashMap<>();
	private static final Random RANDOM = new Random();


	public static Int valueOf(long l)
	{
		Int i = LONG_TO_INT_CACHE.get(l);
		if (i == null)
			LONG_TO_INT_CACHE.put(l, i = new Int(l));
		return i;
	}

	public static Rational valueOf(String s)
	{
		if (s == null || s.isEmpty()) throw new NumberFormatException(String.format("'%s'", s));

		switch (s.toLowerCase())
		{
			case INF_STRING:
				return INF;
			case INFN_STRING:
				return INFN;
			case NAN_STRING_LOW:
				return NAN;
			default:
				Rational q = STRING_TO_RATIONAL_CACHE.get(s);
				if (q == null)
					STRING_TO_RATIONAL_CACHE.put(s, q = fromApRational(parseString(s)));
				return q;
		}
	}

	public static Rational valueOf(double d)
	{
		return valueOf(new Apfloat(d).toString(true));
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
		catch (NumberFormatException ignored)
		{}
		try
		{
			return new Aprational(s);
		}
		catch (NumberFormatException ignored)
		{}
		try
		{
			int dot = s.indexOf(".");
			int scale = -(s.length() - dot - 1);
			String num = s.replaceAll("\\.", "");
			return AprationalMath.scale(new Apint(num), scale);
		}
		catch (NumberFormatException ignored)
		{}

		throw new NumberFormatException();
	}

	public static String pad(String s, int digits)
	{
		if (digits <= 0)
			return s;

		StringBuilder sb = new StringBuilder(s);
		if (sb.indexOf(".") < 0)
			sb.append(".");
		int length = digits - sb.length() + sb.indexOf(".") + 1;
		for (int i = 0; i < length; i++)
			sb.append("0");
		return sb.toString();
	}

	public static Rational random()
	{
		return valueOf(RANDOM.nextDouble());
	}

	public static Real pow(Real x, Real y, long precision)
	{
		/*long precision = minPrecision;
		while (true)
		{
			Real res = x.precision(precision).pow(y);
			if (res.precision() >= minPrecision) return res;
		}*/
		return x.precision(precision).pow(y);
	}


	public static Real log(Real x, long precision)
	{
		return x.precision(precision).log();
	}


	public static Real exp(Real x, long precision)
	{
		return x.precision(precision).exp();
	}


	public static Real niceMidValue(Interval i)
	{
		if (i.min().equals(NAN) || i.max().equals(NAN)) return NAN;
		if (i.min().equals(i.max())) return i.min();
		if (i.min().sgn() < 0 && i.max().sgn() > 0) return ZERO;
		if (i.max().equals(INF)) return ONE.scale(i.min().scale() + 1);
		if (i.min().equals(INFN)) return MINUS_ONE.scale(i.max().scale() + 1);


		Real nice = i.min().add(i.max()).div(2);
		long digits = -nice.scale() - 1;

		Real nicer;
		do
		{
			nicer = nice.round(digits);
			digits++;
		} while (!i.contains(nicer));

		return nicer;
	}
}
