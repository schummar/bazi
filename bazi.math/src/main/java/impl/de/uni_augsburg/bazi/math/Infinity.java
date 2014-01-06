package de.uni_augsburg.bazi.math;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;

import de.uni_augsburg.bazi.common.Json.SerializeAsString;

@SerializeAsString class Infinity implements Int
{
	private final int sgn;

	public Infinity(int sgn)
	{
		this.sgn = sgn;
	}

	// ///////////////////////////////////////////////////////////////////////////

	@Override public Int add(Int that)
	{
		return add((Real) that);
	}

	@Override public Int add(long that)
	{
		return this;
	}

	@Override public Int add(Rational that)
	{
		return add((Real) that);
	}

	@Override public Int add(Real that)
	{
		if (that instanceof NaN)
			return BMath.NAN;
		if (that instanceof Infinity)
			return equals(that) ? this : BMath.NAN;
		return this;
	}

	@Override public Int add(String that)
	{
		return add(BMath.valueOf(that));
	}

	// ///////////////////////////////////////////////////////////////////////////

	@Override public Int ceil()
	{
		return this;
	}

	// ///////////////////////////////////////////////////////////////////////////

	@Override public int compareTo(Int that)
	{
		return compareTo((Real) that);
	}

	@Override public int compareTo(long that)
	{
		return sgn;
	}

	@Override public int compareTo(Rational that)
	{
		return compareTo((Real) that);
	}

	@Override public int compareTo(Real that)
	{
		if (that instanceof NaN)
			throw new RuntimeException("cant compare to NaN");
		if (that instanceof Infinity)
			return sgn() - that.sgn();
		return sgn;
	}

	@Override public int compareTo(String that)
	{
		return compareTo(BMath.valueOf(that));
	}

	// ///////////////////////////////////////////////////////////////////////////

	@Override public List<Int> countTo()
	{
		throw new RuntimeException("cannot count to infinity");
	}

	@Override public List<Int> countTo(Int to)
	{
		throw new RuntimeException("cannot count from infinity");
	}

	// ///////////////////////////////////////////////////////////////////////////

	@Override public Int div(long that)
	{
		return div(BMath.valueOf(that));
	}

	@Override public Int div(Rational that)
	{
		return mul(that.inv());
	}

	@Override public Int div(Real that)
	{
		return mul(that.inv());
	}

	@Override public Int div(String that)
	{
		return div(BMath.valueOf(that));
	}

	// ///////////////////////////////////////////////////////////////////////////

	@Override public boolean equals(Int that)
	{
		return equals((Real) that);
	}

	@Override public boolean equals(long that)
	{
		return false;
	}

	@Override public boolean equals(Rational that)
	{
		return equals((Real) that);
	}

	@Override public boolean equals(Real that)
	{
		if (that instanceof NaN)
			return false;
		if (that instanceof Infinity)
			return sgn == that.sgn();
		return false;
	}

	@Override public boolean equals(String that)
	{
		return equals(BMath.valueOf(that));
	}

	// ///////////////////////////////////////////////////////////////////////////

	@Override public Int floor()
	{
		return this;
	}

	// ///////////////////////////////////////////////////////////////////////////

	@Override public Rational frac()
	{
		return BMath.NAN;
	}

	// ///////////////////////////////////////////////////////////////////////////

	@Override public Int getDenominator()
	{
		return BMath.ONE;
	}

	@Override public Rational getHi()
	{
		return this;
	}

	@Override public Rational getLo()
	{
		return this;
	}

	@Override public Int getNumerator()
	{
		return this;
	}

	@Override public BigInteger getValue()
	{
		throw new RuntimeException("infinity has no big integer representation");
	}

	// ///////////////////////////////////////////////////////////////////////////

	@Override public int intValue()
	{
		throw new RuntimeException("infinity has no int representation");
	}

	// ///////////////////////////////////////////////////////////////////////////

	@Override public Int inv()
	{
		return BMath.ZERO;
	}

	// ///////////////////////////////////////////////////////////////////////////

	@Override public boolean isSpecial()
	{
		return true;
	}

	// ///////////////////////////////////////////////////////////////////////////

	@Override public Iterator<Int> iterator()
	{
		return new IntIterator(BMath.ZERO, this);
	}

	// ///////////////////////////////////////////////////////////////////////////

	@Override public Int max(Int that)
	{
		if (that instanceof NaN)
			return BMath.NAN;
		return sgn > 0 ? this : that;
	}

	@Override public Int max(long that)
	{
		return sgn > 0 ? this : BMath.valueOf(that);
	}

	@Override public Rational max(Rational that)
	{
		if (that instanceof NaN)
			return BMath.NAN;
		return sgn > 0 ? this : that;
	}

	@Override public Real max(Real that)
	{
		if (that instanceof NaN)
			return BMath.NAN;
		return sgn > 0 ? this : that;
	}

	@Override public Rational max(String that)
	{
		return max(BMath.valueOf(that));
	}

	// ///////////////////////////////////////////////////////////////////////////

	@Override public Int min(Int that)
	{
		if (that instanceof NaN)
			return BMath.NAN;
		return sgn < 0 ? this : that;
	}

	@Override public Int min(long that)
	{
		return sgn < 0 ? this : BMath.valueOf(that);
	}

	@Override public Rational min(Rational that)
	{
		if (that instanceof NaN)
			return BMath.NAN;
		return sgn < 0 ? this : that;
	}

	@Override public Real min(Real that)
	{
		if (that instanceof NaN)
			return BMath.NAN;
		return sgn < 0 ? this : that;
	}

	@Override public Rational min(String that)
	{
		return min(BMath.valueOf(that));
	}

	// ///////////////////////////////////////////////////////////////////////////

	@Override public Int mul(Int that)
	{
		return mul((Real) that);
	}

	@Override public Int mul(long that)
	{
		if (that == 0)
			return BMath.NAN;
		return (sgn > 0) == (that > 0) ? BMath.INF : BMath.INFN;
	}

	@Override public Int mul(Rational that)
	{
		return mul((Real) that);
	}

	@Override public Int mul(Real that)
	{
		if (that instanceof NaN)
			return BMath.NAN;
		if (that.sgn() == 0)
			return BMath.NAN;
		return sgn == that.sgn() ? BMath.INF : BMath.INFN;
	}

	@Override public Int mul(String that)
	{
		return mul(BMath.valueOf(that));
	}

	// ///////////////////////////////////////////////////////////////////////////

	@Override public Int neg()
	{
		return sgn < 0 ? BMath.INF : BMath.INFN;
	}

	// ///////////////////////////////////////////////////////////////////////////

	@Override public Rational pow(Int that)
	{
		throw new RuntimeException("'Rational.pow' not yet implemented");
	}

	@Override public Rational pow(long that)
	{
		throw new RuntimeException("'Rational.pow' not yet implemented");
	}

	@Override public Real pow(Real that)
	{
		throw new RuntimeException("'Real.pow' not yet implemented");
	}

	@Override public Real pow(String that)
	{
		throw new RuntimeException("'Real.pow' not yet implemented");
	}

	// ///////////////////////////////////////////////////////////////////////////

	@Override public Int round()
	{
		return this;
	}

	// ///////////////////////////////////////////////////////////////////////////

	@Override public int sgn()
	{
		return sgn;
	}

	// ///////////////////////////////////////////////////////////////////////////

	@Override public Int sub(Int that)
	{
		return add(that.neg());
	}

	@Override public Int sub(long that)
	{
		return add(-that);
	}

	@Override public Int sub(Rational that)
	{
		return add(that.neg());
	}

	@Override public Int sub(Real that)
	{
		return add(that.neg());
	}

	@Override public Int sub(String that)
	{
		return sub(BMath.valueOf(that));
	}

	// ///////////////////////////////////////////////////////////////////////////

	@Override public String toString()
	{
		return sgn > 0 ? BMath.INF_STRING : BMath.INFN_STRING;
	}

	@Override public String toString(int precision)
	{
		return toString();
	}
}
