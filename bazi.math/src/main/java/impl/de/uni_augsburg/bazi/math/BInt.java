package de.uni_augsburg.bazi.math;

import java.math.BigInteger;

class BInt implements Int
{
	public static final Int ONE = new BInt(1);

	private final BigInteger value;

	public BInt(BigInteger value)
	{
		this.value = value;
	}

	public BInt(String s)
	{
		value = new BigInteger(s);
	}

	public BInt(long l)
	{
		value = BigInteger.valueOf(l);
	}

	@Override public Int getNumerator()
	{
		return this;
	}

	@Override public Int getDenominator()
	{
		return ONE;
	}

	@Override public Rational add(Rational that)
	{
		return new BRational(this).add(that);
	}

	@Override public Rational add(String that)
	{
		return new BRational(this).add(that);
	}

	@Override public Rational getLo()
	{
		return this;
	}

	@Override public Rational getHi()
	{
		return this;
	}

	@Override public Real add(Real that)
	{
		return new BReal(this).add(that);
	}

	@Override public String toString(int precision)
	{
		return value.toString();
	}

	@Override public String toString()
	{
		return toString(16);
	}

	@Override public BigInteger getValue()
	{
		return value;
	}

	@Override public Int add(Int that)
	{
		return new BInt(value.add(that.getValue()));
	}

	@Override public Int add(long that)
	{
		return add(new BInt(that));
	}

	@Override public Rational sub(Rational that)
	{
		throw new RuntimeException("'Rational.sub' not yet implemented");
	}

	@Override public Real sub(String that)
	{
		throw new RuntimeException("'Rational.sub' not yet implemented");
	}

	@Override public Rational mul(Rational that)
	{
		throw new RuntimeException("'Rational.mul' not yet implemented");
	}

	@Override public Rational mul(String that)
	{
		throw new RuntimeException("'Rational.mul' not yet implemented");
	}

	@Override public Rational div(Rational that)
	{
		throw new RuntimeException("'Rational.div' not yet implemented");
	}

	@Override public Rational div(String that)
	{
		throw new RuntimeException("'Rational.div' not yet implemented");
	}

	@Override public Rational div(long that)
	{
		throw new RuntimeException("'Rational.div' not yet implemented");
	}

	@Override public Rational min(Rational that)
	{
		throw new RuntimeException("'Rational.min' not yet implemented");
	}

	@Override public Rational min(String that)
	{
		throw new RuntimeException("'Rational.min' not yet implemented");
	}

	@Override public Rational max(Rational that)
	{
		throw new RuntimeException("'Rational.max' not yet implemented");
	}

	@Override public Rational max(String that)
	{
		throw new RuntimeException("'Rational.max' not yet implemented");
	}

	@Override public boolean equals(Rational that)
	{
		throw new RuntimeException("'Rational.equals' not yet implemented");
	}

	@Override public int compare(Rational that)
	{
		throw new RuntimeException("'Rational.compare' not yet implemented");
	}

	@Override public Rational inv()
	{
		throw new RuntimeException("'Rational.inv' not yet implemented");
	}

	@Override public Real sub(Real that)
	{
		throw new RuntimeException("'Real.sub' not yet implemented");
	}

	@Override public Real mul(Real that)
	{
		throw new RuntimeException("'Real.mul' not yet implemented");
	}

	@Override public Real div(Real that)
	{
		throw new RuntimeException("'Real.div' not yet implemented");
	}

	@Override public Real pow(Real that)
	{
		throw new RuntimeException("'Real.pow' not yet implemented");
	}

	@Override public Real pow(String that)
	{
		throw new RuntimeException("'Real.pow' not yet implemented");
	}

	@Override public Real min(Real that)
	{
		throw new RuntimeException("'Real.min' not yet implemented");
	}

	@Override public Real max(Real that)
	{
		throw new RuntimeException("'Real.max' not yet implemented");
	}

	@Override public boolean equals(Real that)
	{
		throw new RuntimeException("'Real.equals' not yet implemented");
	}

	@Override public int compare(Real that)
	{
		throw new RuntimeException("'Real.compare' not yet implemented");
	}

	@Override public Int sub(Int that)
	{
		throw new RuntimeException("'Int.sub' not yet implemented");
	}

	@Override public Int sub(long that)
	{
		throw new RuntimeException("'Int.sub' not yet implemented");
	}

	@Override public Int mul(Int that)
	{
		throw new RuntimeException("'Int.mul' not yet implemented");
	}

	@Override public Int mul(long that)
	{
		throw new RuntimeException("'Int.mul' not yet implemented");
	}

	@Override public Int pow(Int that)
	{
		throw new RuntimeException("'Int.pow' not yet implemented");
	}

	@Override public Int pow(long that)
	{
		throw new RuntimeException("'Int.pow' not yet implemented");
	}

	@Override public Int min(Int that)
	{
		throw new RuntimeException("'Int.min' not yet implemented");
	}

	@Override public Int min(long that)
	{
		throw new RuntimeException("'Int.min' not yet implemented");
	}

	@Override public Int max(Int that)
	{
		throw new RuntimeException("'Int.max' not yet implemented");
	}

	@Override public Int max(long that)
	{
		throw new RuntimeException("'Int.max' not yet implemented");
	}

	@Override public boolean equals(Int that)
	{
		throw new RuntimeException("'Int.equals' not yet implemented");
	}

	@Override public boolean equals(String that)
	{
		throw new RuntimeException("'Int.equals' not yet implemented");
	}

	@Override public boolean equals(long that)
	{
		throw new RuntimeException("'Int.equals' not yet implemented");
	}

	@Override public int compare(Int that)
	{
		throw new RuntimeException("'Int.compare' not yet implemented");
	}

	@Override public int compare(String that)
	{
		throw new RuntimeException("'Int.compare' not yet implemented");
	}

	@Override public int compare(long that)
	{
		throw new RuntimeException("'Int.compare' not yet implemented");
	}

	@Override public Int neg()
	{
		throw new RuntimeException("'Int.neg' not yet implemented");
	}

	@Override public int sgn()
	{
		throw new RuntimeException("'Int.sgn' not yet implemented");
	}
}
