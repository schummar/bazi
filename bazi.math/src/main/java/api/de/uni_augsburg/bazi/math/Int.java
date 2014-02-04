package de.uni_augsburg.bazi.math;

import de.uni_augsburg.bazi.common.Json.DeserializeFromString;
import de.uni_augsburg.bazi.common.Json.SerializeAsString;
import org.apfloat.Apint;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

@SerializeAsString
@DeserializeFromString
public class Int extends Rational
{
	public static Int valueOf(long that)
	{
		return BMath.valueOf(that);
	}
	public static Int valueOf(String that)
	{
		Rational q = BMath.valueOf(that);
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
	public Int(String that)
	{
		this(new Apint(that));
	}
	public Int(long that)
	{
		this(new Apint(that));
	}


	@Override
	public Int add(long that)
	{
		return add(BMath.valueOf(that));
	}
	@Override
	public Rational add(String that)
	{
		return add(BMath.valueOf(that));
	}
	@Override
	public Real add(Real that)
	{
		if (that instanceof Rational)
			return add((Rational) that);
		return that.add(this);
	}
	@Override
	public Rational add(Rational that)
	{
		if (that instanceof Int)
			return add((Int) that);
		return that.add(this);
	}
	public Int add(Int that)
	{
		if (that.isSpecial())
			return that.add(this);
		return new Int(delegate.add(that.delegate));
	}


	@Override
	public Int sub(long that)
	{
		return sub(BMath.valueOf(that));
	}
	@Override
	public Rational sub(String that)
	{
		return sub(BMath.valueOf(that));
	}
	@Override
	public Real sub(Real that)
	{
		if (that instanceof Rational)
			return sub((Rational) that);
		return that.sub(this).neg();
	}
	@Override
	public Rational sub(Rational that)
	{
		if (that instanceof Int)
			return sub((Int) that);
		return that.sub(this).neg();
	}
	public Int sub(Int that)
	{
		if (that.isSpecial())
			return that.sub(this).neg();
		return new Int(delegate.subtract(that.delegate));
	}


	@Override
	public Int mul(long that)
	{
		return mul(BMath.valueOf(that));
	}
	@Override
	public Rational mul(String that)
	{
		return mul(BMath.valueOf(that));
	}
	@Override
	public Real mul(Real that)
	{
		if (that instanceof Rational)
			return mul((Rational) that);
		return that.mul(this);
	}
	@Override
	public Rational mul(Rational that)
	{
		if (that instanceof Int)
			return mul((Int) that);
		return that.mul(this);
	}
	public Int mul(Int that)
	{
		if (that.isSpecial())
			return that.mul(this);
		return new Int(delegate.multiply(that.delegate));
	}


	@Override
	public Int min(long that)
	{
		return min(BMath.valueOf(that));
	}
	@Override
	public Rational min(String that)
	{
		return min(BMath.valueOf(that));
	}
	@Override
	public Real min(Real that)
	{
		if (that instanceof Rational)
			return min((Rational) that);
		return that.min(this);
	}
	@Override
	public Rational min(Rational that)
	{
		if (that instanceof Int)
			return min((Int) that);
		return that.min(this);
	}
	public Int min(Int that)
	{
		if (that.isSpecial())
			return that.min(this);
		return compareTo(that) <= 0 ? this : that;
	}


	@Override
	public Int max(long that)
	{
		return max(BMath.valueOf(that));
	}
	@Override
	public Rational max(String that)
	{
		return max(BMath.valueOf(that));
	}
	@Override
	public Real max(Real that)
	{
		if (that instanceof Rational)
			return max((Rational) that);
		return that.max(this);
	}
	@Override
	public Rational max(Rational that)
	{
		if (that instanceof Int)
			return max((Int) that);
		return that.max(this);
	}
	public Int max(Int that)
	{
		if (that.isSpecial())
			return that.max(this);
		return compareTo(that) >= 0 ? this : that;
	}


	@Override
	public boolean equals(Object that)
	{
		return (this == that)
					 || (that != null && getClass().isInstance(that) && equals((Int) that));
	}
	@Override
	public boolean equals(long that)
	{
		return equals(BMath.valueOf(that));
	}
	@Override
	public boolean equals(String that)
	{
		return equals(BMath.valueOf(that));
	}
	@Override
	public boolean equals(Real that)
	{
		if (that instanceof Rational)
			return equals((Rational) that);
		return that.equals(this);
	}
	@Override
	public boolean equals(Rational that)
	{
		if (that instanceof Int)
			return equals((Int) that);
		return that.equals(this);
	}
	public boolean equals(Int that)
	{
		if (that.isSpecial())
			return that.equals(this);
		return delegate.equals(that.delegate);
	}


	@Override
	public int compareTo(long that)
	{
		return compareTo(BMath.valueOf(that));
	}
	@Override
	public int compareTo(String that)
	{
		return compareTo(BMath.valueOf(that));
	}
	@Override
	public int compareTo(Real that)
	{
		if (that instanceof Rational)
			return compareTo((Rational) that);
		return -that.compareTo(this);
	}
	@Override
	public int compareTo(Rational that)
	{
		if (that instanceof Int)
			return compareTo((Int) that);
		return -that.compareTo(this);
	}
	public int compareTo(Int that)
	{
		if (that.isSpecial())
			return -that.compareTo(this);
		return delegate.compareTo(that.delegate);
	}


	@Override
	public Int neg()
	{
		return new Int(delegate.negate());
	}


	@Override
	public Int abs()
	{
		return sgn() >= 0 ? this : this.neg();
	}


	public int intValue()
	{
		return delegate.intValue();
	}


	@Override
	public String toString()
	{
		return delegate.toString();
	}


	public List<Int> countTo()
	{
		return BMath.ZERO.countTo(this);
	}
	public List<Int> countTo(Int that)
	{
		List<Int> res = new ArrayList<>(sub(that).abs().intValue());
		int incr = -compareTo(that);
		for (Int i = this; !i.equals(that); i = i.add(incr))
			res.add(i);
		return res;
	}

	public void timesDo(Runnable r)
	{
		countTo().stream().forEach(x -> r.run());
	}
	public void timesDo(UnaryOperator<Int> op)
	{
		countTo().stream().forEach(op::apply);
	}
}
