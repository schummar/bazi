package de.uni_augsburg.bazi.math;

import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;
import org.apfloat.Apint;

public class AReal
{
	private final Apfloat delegate;

	AReal(Apfloat delegate)
	{
		this.delegate = delegate;
	}


	public AReal add(long l)
	{
		return add(new AInt(l));
	}
	public AReal add(String s)
	{
		return add(new ARational(s));
	}
	public AReal add(AReal that)
	{
		return new AReal(delegate.add(that.delegate));
	}


	public AReal sub(long l)
	{
		return sub(new AInt(l));
	}
	public AReal sub(String s)
	{
		return sub(new ARational(s));
	}
	public AReal sub(AReal that)
	{
		return new AReal(delegate.subtract(that.delegate));
	}


	public AReal mul(long l)
	{
		return mul(new AInt(l));
	}
	public AReal mul(String s)
	{
		return mul(new ARational(s));
	}
	public AReal mul(AReal that)
	{
		return new AReal(delegate.multiply(that.delegate));
	}


	public AReal div(long l)
	{
		return div(new AInt(l));
	}
	public AReal div(String s)
	{
		return div(new ARational(s));
	}
	public AReal div(AReal that)
	{
		return new AReal(delegate.divide(that.delegate));
	}


	public AReal pow(long l)
	{
		return new AReal(ApfloatMath.pow(delegate, l));
	}
	public AReal pow(String s, int minPrecision)
	{
		return pow(new ARational(s), minPrecision);
	}
	public AReal pow(AReal that, int minPrecision)
	{
		return new AReal(ApfloatMath.pow(delegate, that.delegate));
	}


	public AReal min(long l)
	{
		return min(new AInt(l));
	}
	public AReal min(String s)
	{
		return min(new ARational(s));
	}
	public AReal min(AReal that)
	{
		return compareTo(that) <= 0 ? this : that;
	}


	public AReal max(long l)
	{
		return max(new AInt(l));
	}
	public AReal max(String s)
	{
		return max(new ARational(s));
	}
	public AReal max(AReal that)
	{
		return compareTo(that) >= 0 ? this : that;
	}


	public boolean equals(long l)
	{
		return equals(new AInt(l));
	}
	public boolean equals(String s)
	{
		return equals(new ARational(s));
	}
	public boolean equals(AReal that)
	{
		return delegate.equals(that.delegate);
	}


	public int compareTo(long l)
	{
		return compareTo(new AInt(l));
	}
	public int compareTo(String s)
	{
		return compareTo(new ARational(s));
	}
	public int compareTo(AReal that)
	{
		return delegate.compareTo(that.delegate);
	}


	public AReal neg()
	{
		return new AReal(delegate.negate());
	}


	public AReal inv()
	{
		return new AReal(Apint.ONE.divide(delegate));
	}


	public int sgn()
	{
		return delegate.signum();
	}


	public AInt ceil()
	{
		return new AInt(delegate.ceil());
	}


	public AInt floor()
	{
		return new AInt(delegate.floor());
	}


	public AInt round()
	{
		AInt i = Int();
		return i.compareTo(Const.HALF) >= 0 ? i : i.add(Const.ONE);
	}


	public AInt Int()
	{
		return new AInt(delegate.truncate());
	}


	public AReal frac()
	{
		return sub(Int());
	}


	@Override public String toString()
	{
		return delegate.toString(true);
	}
}
