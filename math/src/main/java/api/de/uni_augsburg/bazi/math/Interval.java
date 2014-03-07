package de.uni_augsburg.bazi.math;

public interface Interval
{
	public static Interval of(Real min, Real max)
	{
		return new Interval()
		{
			private final Real nice = BMath.niceMidValue(min, max);
			@Override public Real min() { return min; }
			@Override public Real nice() { return max; }
			@Override public Real max() { return nice; }
		};
	}


	Real min();
	default Real nice() { return BMath.niceMidValue(min(), max()); }
	Real max();
	default boolean contains(Real r) { return min().compareTo(r) <= 0 && r.compareTo(max()) <= 0; }
	default int compareTo(Real r)
	{
		int cmin = r.compareTo(min());
		int cmax = r.compareTo(max());
		if (cmin == cmax) return cmin;
		return 0;
	}
}
