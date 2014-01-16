package de.uni_augsburg.bazi.math;

import org.apfloat.Aprational;
import org.apfloat.AprationalMath;

import de.uni_augsburg.bazi.common.Json.DeserializeFromString;
import de.uni_augsburg.bazi.common.Json.SerializeAsString;

@SerializeAsString @DeserializeFromString public class Rational extends Real
{
	public static Rational valueOf(String that)
	{
		return BMath.valueOf(that);
	}


	private final Aprational delegate;

	public Rational(Aprational delegate)
	{
		super(delegate);
		this.delegate = delegate;
	}
	public Rational(String that)
	{
		this(BMath.parseString(that));
	}


	@Override public Rational add(long that)
	{
		return add(BMath.valueOf(that));
	}
	@Override public Rational add(String that)
	{
		return add(BMath.valueOf(that));
	}
	@Override public Real add(Real that)
	{
		if (that instanceof Rational)
			return add((Rational) that);
		return that.add(this);
	}
	public Rational add(Rational that)
	{
		if (that.isSpecial())
			return that.add(this);
		return new Rational(delegate.add(that.delegate));
	}


	@Override public Rational sub(long that)
	{
		return sub(BMath.valueOf(that));
	}
	@Override public Rational sub(String that)
	{
		return sub(BMath.valueOf(that));
	}
	@Override public Real sub(Real that)
	{
		if (that instanceof Rational)
			return sub((Rational) that);
		return that.sub(this).neg();
	}
	public Rational sub(Rational that)
	{
		if (that.isSpecial())
			return that.sub(this).neg();
		return new Rational(delegate.subtract(that.delegate));
	}


	@Override public Rational mul(long that)
	{
		return mul(BMath.valueOf(that));
	}
	@Override public Rational mul(String that)
	{
		return mul(BMath.valueOf(that));
	}
	@Override public Real mul(Real that)
	{
		if (that instanceof Rational)
			return mul((Rational) that);
		return that.mul(this);
	}
	public Rational mul(Rational that)
	{
		if (that.isSpecial())
			return that.mul(this);
		return new Rational(delegate.multiply(that.delegate));
	}


	@Override public Rational div(long that)
	{
		return div(BMath.valueOf(that));
	}
	@Override public Rational div(String that)
	{
		return div(BMath.valueOf(that));
	}
	@Override public Real div(Real that)
	{
		if (that instanceof Rational)
			return div((Rational) that);
		return that.div(this).inv();
	}
	public Rational div(Rational that)
	{
		if (that.isSpecial())
			return that.div(this).inv();
		if (that.equals(BMath.ZERO))
			return BMath.INF;
		return new Rational(delegate.divide(that.delegate));
	}


	@Override public Rational pow(long that)
	{
		return new Rational(AprationalMath.pow(delegate, that));
	}


	@Override public Rational min(long that)
	{
		return min(BMath.valueOf(that));
	}
	@Override public Rational min(String that)
	{
		return min(BMath.valueOf(that));
	}
	@Override public Real min(Real that)
	{
		if (that instanceof Rational)
			return min((Rational) that);
		return that.min(this);
	}
	public Rational min(Rational that)
	{
		if (that.isSpecial())
			return that.min(this);
		return compareTo(that) <= 0 ? this : that;
	}


	@Override public Rational max(long that)
	{
		return max(BMath.valueOf(that));
	}
	@Override public Rational max(String that)
	{
		return max(BMath.valueOf(that));
	}
	@Override public Real max(Real that)
	{
		if (that instanceof Rational)
			return max((Rational) that);
		return that.max(this);
	}
	public Rational max(Rational that)
	{
		if (that.isSpecial())
			return that.max(this);
		return compareTo(that) >= 0 ? this : that;
	}


	@Override public boolean equals(long that)
	{
		return equals(BMath.valueOf(that));
	}
	@Override public boolean equals(String that)
	{
		return equals(BMath.valueOf(that));
	}
	@Override public boolean equals(Real that)
	{
		if (that instanceof Rational)
			return equals((Rational) that);
		return that.equals(this);
	}
	public boolean equals(Rational that)
	{
		if (that.isSpecial())
			return that.equals(this);
		return delegate.equals(that.delegate);
	}


	@Override public int compareTo(long that)
	{
		return compareTo(BMath.valueOf(that));
	}
	@Override public int compareTo(String that)
	{
		return compareTo(BMath.valueOf(that));
	}
	@Override public int compareTo(Real that)
	{
		if (that instanceof Rational)
			return compareTo((Rational) that);
		return -that.compareTo(this);
	}
	public int compareTo(Rational that)
	{
		if (that.isSpecial())
			return -that.compareTo(this);
		return delegate.compareTo(that.delegate);
	}


	@Override public Rational neg()
	{
		return new Rational(delegate.negate());
	}


	@Override public Rational inv()
	{
		return BMath.ONE.div(this);
	}


	@Override public Rational abs()
	{
		return sgn() >= 0 ? this : this.neg();
	}


	@Override public Rational frac()
	{
		return sub(Int());
	}


	@Override public String toString()
	{
		return delegate.toString();
	}
}
