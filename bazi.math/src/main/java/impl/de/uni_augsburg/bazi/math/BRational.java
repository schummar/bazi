package de.uni_augsburg.bazi.math;

import java.math.BigInteger;


class BRational implements Rational
{
	private final BigRational value;

	public BRational(BigRational value)
	{
		this.value = value;
	}

	public BRational(Rational q)
	{
		value = new BigRational(q.getNumerator().getValue(), q.getDenominator().getValue());
	}

	public BRational(String s)
	{
		value = new BigRational(s);
	}

	public BRational(long l)
	{
		value = new BigRational(l);
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

	@Override public Int getNumerator()
	{
		return new BInt(value.getNumerator());
	}

	@Override public Int getDenominator()
	{
		return new BInt(value.getDenominator());
	}

	@Override public Rational add(Rational that)
	{
		BigInteger n = that.getNumerator().getValue();
		BigInteger d = that.getDenominator().getValue();
		return new BRational(value.add(new BigRational(n, d)));
	}

	@Override public Rational add(String that)
	{
		return add(new BRational(that));
	}

	@Override public Rational add(long that)
	{
		return add(new BRational(that));
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

	@Override public String toString(int precision)
	{
		return value.toStringDot(precision);
	}

	@Override public String toString()
	{
		return toString(16);
	}

	@Override public Rational sub(Rational that)
	{
		throw new RuntimeException("'Rational.sub' not yet implemented");
	}

	@Override public Real sub(String that)
	{
		throw new RuntimeException("'Rational.sub' not yet implemented");
	}

	@Override public Real sub(long that)
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

	@Override public Rational mul(long that)
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

	@Override public Rational pow(Int that)
	{
		throw new RuntimeException("'Rational.pow' not yet implemented");
	}

	@Override public Rational pow(long that)
	{
		throw new RuntimeException("'Rational.pow' not yet implemented");
	}

	@Override public Rational min(Rational that)
	{
		throw new RuntimeException("'Rational.min' not yet implemented");
	}

	@Override public Rational min(String that)
	{
		throw new RuntimeException("'Rational.min' not yet implemented");
	}

	@Override public Rational min(long that)
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

	@Override public Rational max(long that)
	{
		throw new RuntimeException("'Rational.max' not yet implemented");
	}

	@Override public boolean equals(Rational that)
	{
		throw new RuntimeException("'Rational.equals' not yet implemented");
	}

	@Override public boolean equals(String that)
	{
		throw new RuntimeException("'Rational.equals' not yet implemented");
	}

	@Override public boolean equals(long that)
	{
		throw new RuntimeException("'Rational.equals' not yet implemented");
	}

	@Override public int compare(Rational that)
	{
		throw new RuntimeException("'Rational.compare' not yet implemented");
	}

	@Override public int compare(String that)
	{
		throw new RuntimeException("'Rational.compare' not yet implemented");
	}

	@Override public int compare(long that)
	{
		throw new RuntimeException("'Rational.compare' not yet implemented");
	}

	@Override public Rational neg()
	{
		throw new RuntimeException("'Rational.neg' not yet implemented");
	}

	@Override public Rational inv()
	{
		throw new RuntimeException("'Rational.inv' not yet implemented");
	}

	@Override public int sgn()
	{
		throw new RuntimeException("'Rational.sgn' not yet implemented");
	}


}
