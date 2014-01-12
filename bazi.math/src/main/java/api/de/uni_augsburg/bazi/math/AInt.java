package de.uni_augsburg.bazi.math;

import org.apfloat.Apint;
import org.apfloat.ApintMath;

public class AInt extends ARational
{
	private final Apint delegate;

	AInt(Apint delegate)
	{
		super(delegate);
		this.delegate = delegate;
	}
	public AInt(String s)
	{
		this(new Apint(s));
	}
	public AInt(long l)
	{
		this(new Apint(l));
	}


	@Override public AInt add(long l)
	{
		return add(new AInt(l));
	}
	@Override public ARational add(String s)
	{
		return add(Helper.parseString(s));
	}
	@Override public AReal add(AReal that)
	{
		if (that instanceof ARational)
			return add((ARational) that);
		return that.add(this);
	}
	@Override public ARational add(ARational that)
	{
		if (that instanceof AInt)
			return add(that);
		return that.add(this);
	}
	public AInt add(AInt that)
	{
		return new AInt(delegate.add(that.delegate));
	}


	@Override public AInt sub(long l)
	{
		return sub(new AInt(l));
	}
	@Override public ARational sub(String s)
	{
		return sub(Helper.parseString(s));
	}
	@Override public AReal sub(AReal that)
	{
		if (that instanceof ARational)
			return sub((ARational) that);
		return that.sub(this).neg();
	}
	@Override public ARational sub(ARational that)
	{
		if (that instanceof AInt)
			return sub(that);
		return that.sub(this).neg();
	}
	public AInt sub(AInt that)
	{
		return new AInt(delegate.subtract(that.delegate));
	}


	@Override public AInt mul(long l)
	{
		return mul(new AInt(l));
	}
	@Override public ARational mul(String s)
	{
		return mul(Helper.parseString(s));
	}
	@Override public AReal mul(AReal that)
	{
		if (that instanceof ARational)
			return mul((ARational) that);
		return that.mul(this);
	}
	@Override public ARational mul(ARational that)
	{
		if (that instanceof AInt)
			return mul(that);
		return that.mul(this);
	}
	public AInt mul(AInt that)
	{
		return new AInt(delegate.multiply(that.delegate));
	}


	@Override public AInt pow(long l)
	{
		return new AInt(ApintMath.pow(delegate, l));
	}


	@Override public AInt min(long l)
	{
		return min(new AInt(l));
	}
	@Override public ARational min(String s)
	{
		return min(Helper.parseString(s));
	}
	@Override public AReal min(AReal that)
	{
		if (that instanceof ARational)
			return min((ARational) that);
		return that.min(this);
	}
	@Override public ARational min(ARational that)
	{
		if (that instanceof AInt)
			return min(that);
		return that.min(this);
	}
	public AInt min(AInt that)
	{
		return compareTo(that) <= 0 ? this : that;
	}


	@Override public AInt max(long l)
	{
		return max(new AInt(l));
	}
	@Override public ARational max(String s)
	{
		return max(Helper.parseString(s));
	}
	@Override public AReal max(AReal that)
	{
		if (that instanceof ARational)
			return max((ARational) that);
		return that.max(this);
	}
	@Override public ARational max(ARational that)
	{
		if (that instanceof AInt)
			return max(that);
		return that.max(this);
	}
	public AInt max(AInt that)
	{
		return compareTo(that) >= 0 ? this : that;
	}


	@Override public boolean equals(long l)
	{
		return equals(new AInt(l));
	}
	@Override public boolean equals(String s)
	{
		return equals(Helper.parseString(s));
	}
	@Override public boolean equals(AReal that)
	{
		if (that instanceof ARational)
			return equals((ARational) that);
		return that.equals(this);
	}
	@Override public boolean equals(ARational that)
	{
		if (that instanceof AInt)
			return equals(that);
		return that.equals(this);
	}
	public boolean equals(AInt that)
	{
		return delegate.equals(that.delegate);
	}


	@Override public int compareTo(long l)
	{
		return compareTo(new AInt(l));
	}
	@Override public int compareTo(String s)
	{
		return compareTo(Helper.parseString(s));
	}
	@Override public int compareTo(AReal that)
	{
		if (that instanceof ARational)
			return compareTo((ARational) that);
		return that.compareTo(this);
	}
	@Override public int compareTo(ARational that)
	{
		if (that instanceof AInt)
			return compareTo(that);
		return that.compareTo(this);
	}
	public int compareTo(AInt that)
	{
		return delegate.compareTo(that.delegate);
	}


	@Override public ARational neg()
	{
		return new AInt(delegate.negate());
	}
}
