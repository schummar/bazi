package de.uni_augsburg.bazi.math;


class BReal implements Real
{
	private final Rational lo, hi;

	// //////////////////////////////////////////////////////////////////////////

	public BReal(long l)
	{
		lo = hi = new BRational(l);
	}

	public BReal(Rational lo, Rational hi)
	{
		this.lo = lo;
		this.hi = hi;
	}

	public BReal(Real r)
	{
		lo = r.getLo();
		hi = r.getHi();
	}

	public BReal(String s)
	{
		lo = hi = new BRational(s);
	}

	// //////////////////////////////////////////////////////////////////////////

	@Override public Real add(long that)
	{
		return add(new BReal(that));
	}

	@Override public Real add(Real that)
	{
		return new BReal(lo.add(that.getLo()), hi.add(that.getHi()));
	}

	@Override public Real add(String that)
	{
		return add(new BReal(that));
	}

	// //////////////////////////////////////////////////////////////////////////

	@Override public int compareTo(long that)
	{
		return compareTo(new BReal(that));
	}

	@Override public int compareTo(Real that)
	{
		if (hi.compareTo(that.getLo()) < 0)
			return -1;
		if (lo.compareTo(that.getHi()) > 0)
			return 1;
		return 0;
	}

	@Override public int compareTo(String that)
	{
		return compareTo(new BReal(that));
	}

	// //////////////////////////////////////////////////////////////////////////

	@Override public Real div(long that)
	{
		return div(new BReal(that));
	}

	@Override public Real div(Real that)
	{
		Rational[] qs = {
				lo.div(that.getLo()),
				lo.div(that.getHi()),
				hi.div(that.getLo()),
				hi.div(that.getHi())
		};
		return new BReal(BMath.min(qs), BMath.max(qs));
	}

	@Override public Real div(String that)
	{
		return div(new BReal(that));
	}

	// //////////////////////////////////////////////////////////////////////////

	@Override public boolean equals(long that)
	{
		return equals(new BReal(that));
	}

	@Override public boolean equals(Real that)
	{
		return compareTo(that) == 0;
	}

	@Override public boolean equals(String that)
	{
		return equals(new BReal(that));
	}

	// //////////////////////////////////////////////////////////////////////////

	@Override public Rational getHi()
	{
		return hi;
	}

	@Override public Rational getLo()
	{
		return lo;
	}

	// //////////////////////////////////////////////////////////////////////////

	@Override public Real inv()
	{
		Rational[] qs = { lo.inv(), hi.inv() };
		return new BReal(BMath.min(qs), BMath.max(qs));
	}

	// //////////////////////////////////////////////////////////////////////////

	@Override public Real max(long that)
	{
		return max(new BReal(that));
	}

	@Override public Real max(Real that)
	{
		return new BReal(lo.max(that.getLo()), hi.max(that.getHi()));
	}

	@Override public Real max(String that)
	{
		return max(new BReal(that));
	}

	// //////////////////////////////////////////////////////////////////////////

	@Override public Real min(long that)
	{
		return min(new BReal(that));
	}

	@Override public Real min(Real that)
	{
		return new BReal(lo.min(that.getLo()), hi.min(that.getHi()));
	}

	@Override public Real min(String that)
	{
		return min(new BReal(that));
	}

	// //////////////////////////////////////////////////////////////////////////

	@Override public Real mul(long that)
	{
		return mul(new BReal(that));
	}

	@Override public Real mul(Real that)
	{
		Rational[] qs = {
				lo.mul(that.getLo()),
				lo.mul(that.getHi()),
				hi.mul(that.getLo()),
				hi.mul(that.getHi())
		};
		return new BReal(BMath.min(qs), BMath.max(qs));
	}

	@Override public Real mul(String that)
	{
		return mul(new BReal(that));
	}

	// //////////////////////////////////////////////////////////////////////////

	@Override public Real neg()
	{
		return new BReal(hi.neg(), lo.neg());
	}

	// //////////////////////////////////////////////////////////////////////////

	@Override public Real pow(long that)
	{
		throw new RuntimeException("'BReal.pow' not yet implemented");
	}

	@Override public Real pow(Real that)
	{
		throw new RuntimeException("'BReal.pow' not yet implemented");
	}

	@Override public Real pow(String that)
	{
		throw new RuntimeException("'BReal.pow' not yet implemented");
	}

	// //////////////////////////////////////////////////////////////////////////

	@Override public int sgn()
	{
		return compareTo(0);
	}

	// //////////////////////////////////////////////////////////////////////////

	@Override public Real sub(long that)
	{
		return sub(new BReal(that));
	}

	@Override public Real sub(Real that)
	{
		return new BReal(lo.sub(that.getLo()), hi.sub(that.getHi()));
	}

	@Override public Real sub(String that)
	{
		return sub(new BReal(that));
	}

	// //////////////////////////////////////////////////////////////////////////

	@Override public String toString()
	{
		return toString(16);
	}

	@Override public String toString(int precision)
	{
		return String.format("[%s ; %s]", lo, hi);
	}
}
