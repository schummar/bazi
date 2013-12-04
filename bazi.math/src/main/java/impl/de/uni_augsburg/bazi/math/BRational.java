package de.uni_augsburg.bazi.math;

import java.math.BigInteger;

import de.uni_augsburg.bazi.common.Json.SerializeAsString;

@SerializeAsString class BRational implements Rational
{
	private static BigRational unpack(Rational that)
	{
		BigInteger n = that.getNumerator().getValue();
		BigInteger d = that.getDenominator().getValue();
		return new BigRational(n, d);
	}

	// //////////////////////////////////////////////////////////////////////////

	private final BigRational value;

	// //////////////////////////////////////////////////////////////////////////

	public BRational(BigRational value)
	{
		this.value = value;
	}

	public BRational(long l)
	{
		value = new BigRational(l);
	}

	public BRational(Rational q)
	{
		if (q.isSpecial())
			throw new RuntimeException("cant create BRational from special number");
		value = new BigRational(q.getNumerator().getValue(), q.getDenominator().getValue());
	}

	// //////////////////////////////////////////////////////////////////////////

	@Override public Rational add(long that)
	{
		return add(new BRational(that));
	}

	@Override public Rational add(Rational that)
	{
		if (that.isSpecial())
			return that.add(this);
		return new BRational(value.add(unpack(that)));
	}

	@Override public Real add(Real that)
	{
		return new BReal(this).add(that);
	}

	@Override public Rational add(String that)
	{
		return add(BMath.valueOf(that));
	}

	// //////////////////////////////////////////////////////////////////////////

	@Override public Int ceil()
	{
		return new BInt(value.ceil().getNumerator());
	}

	// //////////////////////////////////////////////////////////////////////////

	@Override public int compareTo(long that)
	{
		return compareTo(new BRational(that));
	}

	@Override public int compareTo(Rational that)
	{
		if (that.isSpecial())
			return -that.compareTo(this);
		return value.compareTo(unpack(that));
	}

	@Override public int compareTo(Real that)
	{
		return new BReal(this).compareTo(that);
	}

	@Override public int compareTo(String that)
	{
		return compareTo(BMath.valueOf(that));
	}

	// //////////////////////////////////////////////////////////////////////////

	@Override public Rational div(long that)
	{
		return div(new BRational(that));
	}

	@Override public Rational div(Rational that)
	{
		return mul(that.inv());
	}

	@Override public Real div(Real that)
	{
		return new BReal(this).div(that);
	}

	@Override public Rational div(String that)
	{
		return div(BMath.valueOf(that));
	}

	// //////////////////////////////////////////////////////////////////////////

	@Override public boolean equals(long that)
	{
		return equals(new BRational(that));
	}

	@Override public boolean equals(Rational that)
	{
		return compareTo(that) == 0;
	}

	@Override public boolean equals(Real that)
	{
		return new BReal(this).equals(that);
	}

	@Override public boolean equals(String that)
	{
		return equals(BMath.valueOf(that));
	}

	// //////////////////////////////////////////////////////////////////////////

	@Override public Int floor()
	{
		return new BInt(value.floor().getNumerator());
	}

	// //////////////////////////////////////////////////////////////////////////

	@Override public Rational frac()
	{
		return sub(floor());
	};

	// //////////////////////////////////////////////////////////////////////////

	@Override public Int getDenominator()
	{
		return new BInt(value.getDenominator());
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
		return new BInt(value.getNumerator());
	}

	// //////////////////////////////////////////////////////////////////////////

	@Override public Rational inv()
	{
		if (value.sign() == 0)
			return BMath.INF;

		return new BRational(value.inv());
	}

	// //////////////////////////////////////////////////////////////////////////

	@Override public boolean isSpecial()
	{
		return false;
	}

	// //////////////////////////////////////////////////////////////////////////

	@Override public Rational max(long that)
	{
		return max(new BRational(that));
	}

	@Override public Rational max(Rational that)
	{
		if (that.isSpecial())
			return that.max(this);
		return compareTo(that) >= 0 ? this : that;
	}

	@Override public Real max(Real that)
	{
		return new BReal(this).max(that);
	}

	@Override public Rational max(String that)
	{
		return max(BMath.valueOf(that));
	}

	// //////////////////////////////////////////////////////////////////////////

	@Override public Rational min(long that)
	{
		return min(new BRational(that));
	}

	@Override public Rational min(Rational that)
	{
		if (that.isSpecial())
			return that.min(this);
		return compareTo(that) <= 0 ? this : that;
	}

	@Override public Real min(Real that)
	{
		return new BReal(this).min(that);
	}

	@Override public Rational min(String that)
	{
		return min(BMath.valueOf(that));
	}

	// //////////////////////////////////////////////////////////////////////////

	@Override public Rational mul(long that)
	{
		return mul(new BRational(that));
	}

	@Override public Rational mul(Rational that)
	{
		if (that.isSpecial())
			return that.mul(this);
		return new BRational(value.mul(unpack(that)));
	}

	@Override public Real mul(Real that)
	{
		return new BReal(this).mul(that);
	}

	@Override public Rational mul(String that)
	{
		return mul(BMath.valueOf(that));
	}

	// //////////////////////////////////////////////////////////////////////////

	@Override public Rational neg()
	{
		return new BRational(value.neg());
	}

	// //////////////////////////////////////////////////////////////////////////

	@Override public Rational pow(Int that)
	{
		if (that.isSpecial())
		{
			if (that.equals(BMath.INF))
				return BMath.INF;
			if (that.equals(BMath.INFN))
				return BMath.ZERO;
			return BMath.NAN;
		}

		if (sgn() == 0)
			return this;
		if (that.sgn() == 0)
			return BMath.ONE;

		boolean inv = that.sgn() < 0;
		if (inv)
			that = that.neg();
		BigRational pow = BigRational.ONE;

		if (that.compareTo(Integer.MAX_VALUE) > 0)
		{
			BigRational maxpow = value.pow(Integer.MAX_VALUE);
			while (that.compareTo(Integer.MAX_VALUE) > 0)
			{
				that = that.sub(Integer.MAX_VALUE);
				pow = pow.mul(maxpow);
			}
		}
		pow = pow.mul(value.pow(that.getValue().intValue()));

		return new BRational(inv ? pow.inv() : pow);
	}

	@Override public Rational pow(long that)
	{
		return pow(new BInt(that));
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
		return new BInt(value.round().getNumerator());
	}


	// //////////////////////////////////////////////////////////////////////////

	@Override public int sgn()
	{
		return compareTo(BMath.ZERO);
	}

	// //////////////////////////////////////////////////////////////////////////

	@Override public Rational sub(long that)
	{
		return sub(new BRational(that));
	}

	@Override public Rational sub(Rational that)
	{
		if (that.isSpecial())
			return that.sub(this).neg();
		return add(that.neg());
	}

	@Override public Real sub(Real that)
	{
		return new BReal(this).sub(that);
	}

	@Override public Rational sub(String that)
	{
		return sub(BMath.valueOf(that));
	}

	// //////////////////////////////////////////////////////////////////////////

	@Override public String toString()
	{
		String dot = toString(BMath.DEFAULT_PRECISION);
		if (equals(BMath.valueOf(dot)))
			return dot;
		return value.toString();
	}

	@Override public String toString(int precision)
	{
		return value.toStringDot(precision)
				.replaceAll("0*$", "")
				.replaceAll("\\.$", "");
	}
}
