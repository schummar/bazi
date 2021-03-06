package de.uni_augsburg.bazi.math;

public interface Interval
{
	public static Interval of(Real min, Real max)
	{
		return new Interval()
		{
			@Override public Real min() { return min; }
			@Override public Real max() { return max; }
		};
	}


	Real min();
	default Real nice() { return BMath.niceMidValue(this); }
	Real max();
	default boolean contains(Real r)
	{
		if (min().equals(BMath.NAN) || max().equals(BMath.NAN) || r.equals(BMath.NAN)) return false;
		return min().compareTo(r) <= 0 && r.compareTo(max()) <= 0;
	}
	default int compareTo(Real r)
	{
		if (min().equals(BMath.NAN) || max().equals(BMath.NAN) || r.equals(BMath.NAN)) return 0;

		int cmin = r.compareTo(min());
		int cmax = r.compareTo(max());
		if (cmin == cmax) return cmin;
		return 0;
	}
}
