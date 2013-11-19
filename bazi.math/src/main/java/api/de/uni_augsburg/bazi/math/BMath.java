package de.uni_augsburg.bazi.math;

public class BMath
{
	public static final Int INF = new BInt(0), INFN = new BInt(0), NAN = new BInt(0);

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
}
