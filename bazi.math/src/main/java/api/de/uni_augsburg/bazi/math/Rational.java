package de.uni_augsburg.bazi.math;

import org.apfloat.Apint;
import org.apfloat.Aprational;
import org.apfloat.AprationalMath;

import de.uni_augsburg.bazi.common.Json.DeserializeFromString;
import de.uni_augsburg.bazi.common.Json.SerializeAsString;

@SerializeAsString @DeserializeFromString public class Rational extends Real
{
	public static Rational valueOf(String s)
	{
		return BMath.valueOf(s);
	}


	private final Aprational delegate;

	public Rational(Aprational delegate)
	{
		super(delegate);
		this.delegate = delegate;
	}
	public Rational(String s)
	{
		this(BMath.parseString(s));
	}


	@Override public Rational add(long l)
	{
		return add(new Int(l));
	}
	@Override public Rational add(String s)
	{
		return add(new Rational(s));
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


	@Override public Rational sub(long l)
	{
		return sub(new Int(l));
	}
	@Override public Rational sub(String s)
	{
		return sub(new Rational(s));
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


	@Override public Rational mul(long l)
	{
		return mul(new Int(l));
	}
	@Override public Rational mul(String s)
	{
		return mul(new Rational(s));
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


	@Override public Rational div(long l)
	{
		return div(new Int(l));
	}
	@Override public Rational div(String s)
	{
		return div(new Rational(s));
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
		return new Rational(delegate.divide(that.delegate));
	}


	@Override public Rational pow(long l)
	{
		return new Rational(AprationalMath.pow(delegate, l));
	}


	@Override public Rational min(long l)
	{
		return min(new Int(l));
	}
	@Override public Rational min(String s)
	{
		return min(new Rational(s));
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


	@Override public Rational max(long l)
	{
		return max(new Int(l));
	}
	@Override public Rational max(String s)
	{
		return max(new Rational(s));
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


	@Override public boolean equals(long l)
	{
		return equals(new Int(l));
	}
	@Override public boolean equals(String s)
	{
		return equals(new Rational(s));
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


	@Override public int compareTo(long l)
	{
		return compareTo(new Int(l));
	}
	@Override public int compareTo(String s)
	{
		return compareTo(new Rational(s));
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
		return new Rational(Apint.ONE.divide(delegate));
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
