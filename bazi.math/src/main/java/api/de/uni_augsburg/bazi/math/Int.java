package de.uni_augsburg.bazi.math;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

import org.apfloat.Apint;

import de.uni_augsburg.bazi.common.Json.SerializeAsString;

@SerializeAsString public class Int extends Rational
{
	public static Int valueOf(String s)
	{
		Rational q = BMath.valueOf(s);
		if (q instanceof Int)
			return (Int) q;
		throw new NumberFormatException();
	}


	private final Apint delegate;

	Int(Apint delegate)
	{
		super(delegate);
		this.delegate = delegate;
	}
	public Int(String s)
	{
		this(new Apint(s));
	}
	public Int(long l)
	{
		this(new Apint(l));
	}


	@Override public Int add(long l)
	{
		return add(new Int(l));
	}
	@Override public Rational add(String s)
	{
		return add(BMath.valueOf(s));
	}
	@Override public Real add(Real that)
	{
		if (that instanceof Rational)
			return add((Rational) that);
		return that.add(this);
	}
	@Override public Rational add(Rational that)
	{
		if (that instanceof Int)
			return add((Int) that);
		return that.add(this);
	}
	public Int add(Int that)
	{
		return new Int(delegate.add(that.delegate));
	}


	@Override public Int sub(long l)
	{
		return sub(new Int(l));
	}
	@Override public Rational sub(String s)
	{
		return sub(BMath.valueOf(s));
	}
	@Override public Real sub(Real that)
	{
		if (that instanceof Rational)
			return sub((Rational) that);
		return that.sub(this).neg();
	}
	@Override public Rational sub(Rational that)
	{
		if (that instanceof Int)
			return sub((Int) that);
		return that.sub(this).neg();
	}
	public Int sub(Int that)
	{
		return new Int(delegate.subtract(that.delegate));
	}


	@Override public Int mul(long l)
	{
		return mul(new Int(l));
	}
	@Override public Rational mul(String s)
	{
		return mul(BMath.valueOf(s));
	}
	@Override public Real mul(Real that)
	{
		if (that instanceof Rational)
			return mul((Rational) that);
		return that.mul(this);
	}
	@Override public Rational mul(Rational that)
	{
		if (that instanceof Int)
			return mul((Int) that);
		return that.mul(this);
	}
	public Int mul(Int that)
	{
		return new Int(delegate.multiply(that.delegate));
	}


	@Override public Int min(long l)
	{
		return min(new Int(l));
	}
	@Override public Rational min(String s)
	{
		return min(BMath.valueOf(s));
	}
	@Override public Real min(Real that)
	{
		if (that instanceof Rational)
			return min((Rational) that);
		return that.min(this);
	}
	@Override public Rational min(Rational that)
	{
		if (that instanceof Int)
			return min((Int) that);
		return that.min(this);
	}
	public Int min(Int that)
	{
		return compareTo(that) <= 0 ? this : that;
	}


	@Override public Int max(long l)
	{
		return max(new Int(l));
	}
	@Override public Rational max(String s)
	{
		return max(BMath.valueOf(s));
	}
	@Override public Real max(Real that)
	{
		if (that instanceof Rational)
			return max((Rational) that);
		return that.max(this);
	}
	@Override public Rational max(Rational that)
	{
		if (that instanceof Int)
			return max((Int) that);
		return that.max(this);
	}
	public Int max(Int that)
	{
		return compareTo(that) >= 0 ? this : that;
	}


	@Override public boolean equals(long l)
	{
		return equals(new Int(l));
	}
	@Override public boolean equals(String s)
	{
		return equals(BMath.parseString(s));
	}
	@Override public boolean equals(Real that)
	{
		if (that instanceof Rational)
			return equals((Rational) that);
		return that.equals(this);
	}
	@Override public boolean equals(Rational that)
	{
		if (that instanceof Int)
			return equals((Int) that);
		return that.equals(this);
	}
	public boolean equals(Int that)
	{
		return delegate.equals(that.delegate);
	}


	@Override public int compareTo(long l)
	{
		return compareTo(new Int(l));
	}
	@Override public int compareTo(String s)
	{
		return compareTo(BMath.valueOf(s));
	}
	@Override public int compareTo(Real that)
	{
		if (that instanceof Rational)
			return compareTo((Rational) that);
		return that.compareTo(this);
	}
	@Override public int compareTo(Rational that)
	{
		if (that instanceof Int)
			return compareTo((Int) that);
		return that.compareTo(this);
	}
	public int compareTo(Int that)
	{
		return delegate.compareTo(that.delegate);
	}


	@Override public Int neg()
	{
		return new Int(delegate.negate());
	}


	public int intValue()
	{
		return delegate.intValue();
	}


	@Override public String toString()
	{
		return delegate.toString();
	}


	public List<Int> countTo()
	{
		return BMath.ZERO.countTo(this);
	}
	public List<Int> countTo(Int to)
	{
		List<Int> res = new ArrayList<Int>();
		for (Int i = this; i.compareTo(to) < 0; i = i.add(1))
			res.add(i);
		return res;
	}


	public void timesDo(Runnable r)
	{
		countTo().stream().forEach(x -> r.run());
	}
	public void timesDo(UnaryOperator<Int> op)
	{
		countTo().stream().forEach(x -> op.apply(x));
	}
}
