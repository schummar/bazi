package de.uni_augsburg.bazi.math;


class BReal implements Real
{
	private final Rational lo, hi;

	// //////////////////////////////////////////////////////////////////////////

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

	public BReal(long l)
	{
		lo = hi = new BRational(l);
	}

	// //////////////////////////////////////////////////////////////////////////

	@Override public Rational getLo()
	{
		return lo;
	}

	@Override public Rational getHi()
	{
		return hi;
	}

	// //////////////////////////////////////////////////////////////////////////

	@Override public Real add(Real that)
	{
		return new BReal(lo.add(that.getLo()), hi.add(that.getHi()));
	}

	@Override public Real add(String that)
	{
		return add(new BReal(that));
	}

	@Override public Real add(long that)
	{
		return add(new BReal(that));
	}

	// //////////////////////////////////////////////////////////////////////////

	@Override public Real sub(Real that)
	{
		return new BReal(lo.sub(that.getLo()), hi.sub(that.getHi()));
	}

	@Override public Real sub(String that)
	{
		return sub(new BReal(that));
	}

	@Override public Real sub(long that)
	{
		return sub(new BReal(that));
	}

	// //////////////////////////////////////////////////////////////////////////

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

	@Override public Real mul(long that)
	{
		return mul(new BReal(that));
	}

	// //////////////////////////////////////////////////////////////////////////

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

	@Override public Real div(long that)
	{
		return div(new BReal(that));
	}

	// //////////////////////////////////////////////////////////////////////////

	@Override public Real pow(Real that)
	{
		throw new RuntimeException("'BReal.pow' not yet implemented");
	}

	@Override public Real pow(String that)
	{
		throw new RuntimeException("'BReal.pow' not yet implemented");
	}

	@Override public Real pow(long that)
	{
		throw new RuntimeException("'BReal.pow' not yet implemented");
	}

	// //////////////////////////////////////////////////////////////////////////

	@Override public Real min(Real that)
	{
		throw new RuntimeException("'BReal.min' not yet implemented");
	}

	@Override public Real min(String that)
	{
		throw new RuntimeException("'BReal.min' not yet implemented");
	}

	@Override public Real min(long that)
	{
		throw new RuntimeException("'BReal.min' not yet implemented");
	}

	// //////////////////////////////////////////////////////////////////////////

	@Override public Real max(Real that)
	{
		throw new RuntimeException("'BReal.min' not yet implemented");
	};

	@Override public Real max(String that)
	{
		throw new RuntimeException("'BReal.max' not yet implemented");
	}

	@Override public Real max(long that)
	{
		throw new RuntimeException("'BReal.max' not yet implemented");
	}

	// //////////////////////////////////////////////////////////////////////////

	@Override public boolean equals(Real that)
	{
		throw new RuntimeException("'BReal.min' not yet implemented");
	}

	@Override public boolean equals(String that)
	{
		throw new RuntimeException("'BReal.min' not yet implemented");
	}

	@Override public boolean equals(long that)
	{
		throw new RuntimeException("'BReal.min' not yet implemented");
	}

	// //////////////////////////////////////////////////////////////////////////

	@Override public int compare(Real that)
	{
		throw new RuntimeException("'BReal.min' not yet implemented");
	}

	@Override public int compare(String that)
	{
		throw new RuntimeException("'BReal.min' not yet implemented");
	}

	@Override public int compare(long that)
	{
		throw new RuntimeException("'BReal.min' not yet implemented");
	}

	// //////////////////////////////////////////////////////////////////////////

	@Override public Real neg()
	{
		throw new RuntimeException("'BReal.min' not yet implemented");
	}

	@Override public Real inv()
	{
		throw new RuntimeException("'BReal.min' not yet implemented");
	}

	@Override public int sgn()
	{
		throw new RuntimeException("'BReal.min' not yet implemented");
	}

	// //////////////////////////////////////////////////////////////////////////

	@Override public String toString(int precision)
	{
		return String.format("[%s ; %s]", lo, hi);
	}

	@Override public String toString()
	{
		return toString(16);
	}
}
