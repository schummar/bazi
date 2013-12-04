package de.uni_augsburg.bazi.math;

import java.math.BigInteger;

import de.uni_augsburg.bazi.common.Json.SerializeAsString;
import de.uni_augsburg.bazi.common.Resources;

@SerializeAsString class BInt implements Int
{

	// //////////////////////////////////////////////////////////////////////////

	private final BigInteger value;

	// //////////////////////////////////////////////////////////////////////////

	public BInt(BigInteger value)
	{
		this.value = value;
	}

	public BInt(long l)
	{
		value = BigInteger.valueOf(l);
	}

	public BInt(Int i)
	{
		if (i.isSpecial())
			throw new RuntimeException("cant create BInt from special number");
		value = i.getValue();
	}

	// //////////////////////////////////////////////////////////////////////////

	@Override public Int add(Int that)
	{
		if (that.isSpecial())
			return that.add(this);
		return new BInt(value.add(that.getValue()));
	}

	@Override public Int add(long that)
	{
		return add(new BInt(that));
	}

	@Override public Rational add(Rational that)
	{
		return new BRational(this).add(that);
	}

	@Override public Real add(Real that)
	{
		return new BReal(this).add(that);
	}

	@Override public Rational add(String that)
	{
		return new BRational(this).add(that);
	}

	// //////////////////////////////////////////////////////////////////////////

	@Override public Int ceil()
	{
		return this;
	};

	// //////////////////////////////////////////////////////////////////////////

	@Override public int compareTo(Int that)
	{
		if (that.isSpecial())
			return -that.compareTo(this);
		return value.compareTo(that.getValue());
	}

	@Override public int compareTo(long that)
	{
		return compareTo(new BInt(that));
	}

	@Override public int compareTo(Rational that)
	{
		return new BRational(this).compareTo(that);
	}

	@Override public int compareTo(Real that)
	{
		return new BReal(this).compareTo(that);
	}

	@Override public int compareTo(String that)
	{
		return new BRational(this).compareTo(that);
	}

	// //////////////////////////////////////////////////////////////////////////

	@Override public Rational div(long that)
	{
		return new BRational(this).div(that);
	}

	@Override public Rational div(Rational that)
	{
		return new BRational(this).div(that);
	}

	@Override public Real div(Real that)
	{
		return new BReal(this).div(that);
	}

	@Override public Rational div(String that)
	{
		return new BRational(this).div(that);
	}

	// //////////////////////////////////////////////////////////////////////////

	@Override public boolean equals(Int that)
	{
		if (that.isSpecial())
			return that.equals(this);
		return value.equals(that.getValue());
	}

	@Override public boolean equals(long that)
	{
		return equals(new BInt(that));
	}

	@Override public boolean equals(Rational that)
	{
		return new BRational(this).equals(that);
	}

	@Override public boolean equals(Real that)
	{
		return new BReal(this).equals(that);
	}

	@Override public boolean equals(String that)
	{
		return new BRational(this).equals(that);
	}

	// //////////////////////////////////////////////////////////////////////////

	@Override public Int floor()
	{
		return this;
	}

	// //////////////////////////////////////////////////////////////////////////

	@Override public Rational frac()
	{
		return BMath.ZERO;
	}

	// //////////////////////////////////////////////////////////////////////////

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
		return value;
	}

	// //////////////////////////////////////////////////////////////////////////

	@Override public int intValue()
	{
		if (compareTo(Integer.MAX_VALUE) > 0)
			throw new RuntimeException(Resources.get("int.intvalue"));
		return value.intValue();
	}

	// //////////////////////////////////////////////////////////////////////////

	@Override public Rational inv()
	{
		return new BRational(this).inv();
	}

	// //////////////////////////////////////////////////////////////////////////

	@Override public boolean isSpecial()
	{
		return false;
	}

	// //////////////////////////////////////////////////////////////////////////

	@Override public Int max(Int that)
	{
		if (that.isSpecial())
			return that.max(this);
		return compareTo(that) >= 0 ? this : that;
	}

	@Override public Int max(long that)
	{
		return max(new BInt(that));
	}

	@Override public Rational max(Rational that)
	{
		return new BRational(this).max(that);
	}

	@Override public Real max(Real that)
	{
		return new BReal(this).max(that);
	}

	@Override public Rational max(String that)
	{
		return new BRational(this).max(that);
	}

	// //////////////////////////////////////////////////////////////////////////

	@Override public Int min(Int that)
	{
		if (that.isSpecial())
			return that.min(this);
		return compareTo(that) <= 0 ? this : that;
	}

	@Override public Int min(long that)
	{
		return min(new BInt(that));
	}

	@Override public Rational min(Rational that)
	{
		return new BRational(this).min(that);
	}

	@Override public Real min(Real that)
	{
		return new BReal(this).min(that);
	}

	@Override public Rational min(String that)
	{
		return new BRational(this).min(that);
	}

	// //////////////////////////////////////////////////////////////////////////

	@Override public Int mul(Int that)
	{
		if (that.isSpecial())
			return that.mul(this);
		return new BInt(value.multiply(that.getValue()));
	}

	@Override public Int mul(long that)
	{
		return mul(new BInt(that));
	}

	@Override public Rational mul(Rational that)
	{
		return new BRational(this).mul(that);
	}

	@Override public Real mul(Real that)
	{
		return new BReal(this).mul(that);
	}

	@Override public Rational mul(String that)
	{
		return new BRational(this).mul(that);
	}

	// //////////////////////////////////////////////////////////////////////////

	@Override public Int neg()
	{
		return new BInt(value.negate());
	}

	// //////////////////////////////////////////////////////////////////////////

	@Override public Rational pow(Int that)
	{
		return new BRational(this).pow(that);
	}

	@Override public Rational pow(long that)
	{
		return new BRational(this).pow(that);
	}

	@Override public Real pow(Real that)
	{
		return new BReal(this).pow(that);
	}

	@Override public Real pow(String that)
	{
		return new BReal(this).pow(that);
	}

	// //////////////////////////////////////////////////////////////////////////

	@Override public Int round()
	{
		return this;
	}

	// //////////////////////////////////////////////////////////////////////////

	@Override public int sgn()
	{
		return compareTo(0);
	}

	// //////////////////////////////////////////////////////////////////////////

	@Override public Int sub(Int that)
	{
		if (that.isSpecial())
			return that.sub(this).neg();
		return add(that.neg());
	}

	@Override public Int sub(long that)
	{
		return sub(new BInt(that));
	}

	@Override public Rational sub(Rational that)
	{
		return new BRational(this).sub(that);
	}

	@Override public Real sub(Real that)
	{
		return new BReal(this).sub(that);
	}

	@Override public Rational sub(String that)
	{
		return new BRational(this).sub(that);
	}

	// //////////////////////////////////////////////////////////////////////////

	@Override public String toString()
	{
		return toString(16);
	}

	@Override public String toString(int precision)
	{
		return value.toString();
	}
}
