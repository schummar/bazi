package de.uni_augsburg.bazi.math;

import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;
import org.apfloat.Apint;

import de.uni_augsburg.bazi.common.Json.DeserializeFromString;
import de.uni_augsburg.bazi.common.Json.SerializeAsString;

@SerializeAsString @DeserializeFromString public class Real implements Comparable<Real>
{
	public static Real valueOf(String s)
	{
		return BMath.valueOf(s);
	}


	private final Apfloat delegate;

	Real(Apfloat delegate)
	{
		this.delegate = delegate;
	}


	public boolean isSpecial()
	{
		return false;
	}

	public Real add(long l)
	{
		return add(new Int(l));
	}
	public Real add(String s)
	{
		return add(new Rational(s));
	}
	public Real add(Real that)
	{
		if (that.isSpecial())
			return that.add(this);
		return new Real(delegate.add(that.delegate));
	}


	public Real sub(long l)
	{
		return sub(new Int(l));
	}
	public Real sub(String s)
	{
		return sub(new Rational(s));
	}
	public Real sub(Real that)
	{
		if (that.isSpecial())
			return that.sub(this).neg();
		return new Real(delegate.subtract(that.delegate));
	}


	public Real mul(long l)
	{
		return mul(new Int(l));
	}
	public Real mul(String s)
	{
		return mul(new Rational(s));
	}
	public Real mul(Real that)
	{
		if (that.isSpecial())
			return that.mul(this);
		return new Real(delegate.multiply(that.delegate));
	}


	public Real div(long l)
	{
		return div(new Int(l));
	}
	public Real div(String s)
	{
		return div(new Rational(s));
	}
	public Real div(Real that)
	{
		if (that.isSpecial())
			return that.div(this).inv();
		return new Real(delegate.divide(that.delegate));
	}


	public Real pow(long l)
	{
		return new Real(ApfloatMath.pow(delegate, l));
	}
	public Real pow(String s)
	{
		return pow(new Rational(s));
	}
	public Real pow(Real that)
	{
		if (that.isSpecial())
			return BMath.NAN;
		return new Real(ApfloatMath.pow(delegate, that.delegate));
	}


	public Real min(long l)
	{
		return min(new Int(l));
	}
	public Real min(String s)
	{
		return min(new Rational(s));
	}
	public Real min(Real that)
	{
		if (that.isSpecial())
			return that.min(this);
		return compareTo(that) <= 0 ? this : that;
	}


	public Real max(long l)
	{
		return max(new Int(l));
	}
	public Real max(String s)
	{
		return max(new Rational(s));
	}
	public Real max(Real that)
	{
		if (that.isSpecial())
			return that.max(this);
		return compareTo(that) >= 0 ? this : that;
	}


	public boolean equals(long l)
	{
		return equals(new Int(l));
	}
	public boolean equals(String s)
	{
		return equals(new Rational(s));
	}
	public boolean equals(Real that)
	{
		if (that.isSpecial())
			return that.equals(this);
		return delegate.equals(that.delegate);
	}


	public int compareTo(long l)
	{
		return compareTo(new Int(l));
	}
	public int compareTo(String s)
	{
		return compareTo(new Rational(s));
	}
	@Override public int compareTo(Real that)
	{
		if (that.isSpecial())
			return -that.compareTo(this);
		return delegate.compareTo(that.delegate);
	}


	public Real neg()
	{
		return new Real(delegate.negate());
	}


	public Real inv()
	{
		return new Real(Apint.ONE.divide(delegate));
	}


	public Real abs()
	{
		return sgn() >= 0 ? this : this.neg();
	}


	public int sgn()
	{
		return delegate.signum();
	}


	public Int ceil()
	{
		return new Int(delegate.ceil());
	}


	public Int floor()
	{
		return new Int(delegate.floor());
	}


	public Int round()
	{
		return frac().compareTo(BMath.HALF) < 0 ? Int() : Int().add(BMath.ONE);
	}


	public Int Int()
	{
		return new Int(delegate.truncate());
	}


	public Real frac()
	{
		return sub(Int());
	}


	@Override public String toString()
	{
		return delegate.toString(true);
	}

	public String toString(int digits)
	{
		Rational x = BMath.TEN.pow(digits);
		return BMath.pad(((Real) mul(x).round().div(x)).delegate.precision(digits + 1).toString(true), digits);

	}
}
