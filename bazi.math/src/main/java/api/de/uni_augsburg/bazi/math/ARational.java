package de.uni_augsburg.bazi.math;

import org.apfloat.Apint;
import org.apfloat.Aprational;
import org.apfloat.AprationalMath;

public class ARational extends AReal
{
	private final Aprational delegate;

	public ARational(Aprational delegate)
	{
		super(delegate);
		this.delegate = delegate;
	}
	public ARational(String s)
	{
		this(Helper.aprational(s));
	}


	@Override public ARational add(long l)
	{
		return add(new AInt(l));
	}
	@Override public ARational add(String s)
	{
		return add(new ARational(s));
	}
	@Override public AReal add(AReal that)
	{
		if (that instanceof ARational)
			return add((ARational) that);
		return that.add(this);
	}
	public ARational add(ARational that)
	{
		return new ARational(delegate.add(that.delegate));
	}


	@Override public ARational sub(long l)
	{
		return sub(new AInt(l));
	}
	@Override public ARational sub(String s)
	{
		return sub(new ARational(s));
	}
	@Override public AReal sub(AReal that)
	{
		if (that instanceof ARational)
			return sub((ARational) that);
		return that.sub(this).neg();
	}
	public ARational sub(ARational that)
	{
		return new ARational(delegate.subtract(that.delegate));
	}


	@Override public ARational mul(long l)
	{
		return mul(new AInt(l));
	}
	@Override public ARational mul(String s)
	{
		return mul(new ARational(s));
	}
	@Override public AReal mul(AReal that)
	{
		if (that instanceof ARational)
			return mul((ARational) that);
		return that.mul(this);
	}
	public ARational mul(ARational that)
	{
		return new ARational(delegate.multiply(that.delegate));
	}


	@Override public ARational div(long l)
	{
		return div(new AInt(l));
	}
	@Override public ARational div(String s)
	{
		return div(new ARational(s));
	}
	@Override public AReal div(AReal that)
	{
		if (that instanceof ARational)
			return div((ARational) that);
		return that.div(this).inv();
	}
	public ARational div(ARational that)
	{
		return new ARational(delegate.divide(that.delegate));
	}


	@Override public ARational pow(long l)
	{
		return new ARational(AprationalMath.pow(delegate, l));
	}


	@Override public ARational min(long l)
	{
		return min(new AInt(l));
	}
	@Override public ARational min(String s)
	{
		return min(new ARational(s));
	}
	@Override public AReal min(AReal that)
	{
		if (that instanceof ARational)
			return min((ARational) that);
		return that.min(this);
	}
	public ARational min(ARational that)
	{
		return compareTo(that) <= 0 ? this : that;
	}


	@Override public ARational max(long l)
	{
		return max(new AInt(l));
	}
	@Override public ARational max(String s)
	{
		return max(new ARational(s));
	}
	@Override public AReal max(AReal that)
	{
		if (that instanceof ARational)
			return max((ARational) that);
		return that.max(this);
	}
	public ARational max(ARational that)
	{
		return compareTo(that) >= 0 ? this : that;
	}


	@Override public boolean equals(long l)
	{
		return equals(new AInt(l));
	}
	@Override public boolean equals(String s)
	{
		return equals(new ARational(s));
	}
	@Override public boolean equals(AReal that)
	{
		if (that instanceof ARational)
			return equals((ARational) that);
		return that.equals(this);
	}
	public boolean equals(ARational that)
	{
		return delegate.equals(that.delegate);
	}


	@Override public int compareTo(long l)
	{
		return compareTo(new AInt(l));
	}
	@Override public int compareTo(String s)
	{
		return compareTo(new ARational(s));
	}
	@Override public int compareTo(AReal that)
	{
		if (that instanceof ARational)
			return compareTo((ARational) that);
		return -that.compareTo(this);
	}
	public int compareTo(ARational that)
	{
		return delegate.compareTo(that.delegate);
	}


	@Override public ARational neg()
	{
		return new ARational(delegate.negate());
	}


	@Override public ARational inv()
	{
		return new ARational(Apint.ONE.divide(delegate));
	}


	@Override public String toString()
	{
		return delegate.toString();
	}
}
