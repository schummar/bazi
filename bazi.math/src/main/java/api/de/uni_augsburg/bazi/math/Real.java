package de.uni_augsburg.bazi.math;

import com.sun.istack.internal.NotNull;
import de.uni_augsburg.bazi.common.Json.DeserializeFromString;
import de.uni_augsburg.bazi.common.Json.SerializeAsString;
import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;
import org.apfloat.Apint;

@SerializeAsString
@DeserializeFromString
public class Real implements Comparable<Real>
{
	public static Real valueOf(String that)
	{
		return BMath.valueOf(that);
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


	public Real add(long that)
	{
		return add(BMath.valueOf(that));
	}
	public Real add(String that)
	{
		return add(BMath.valueOf(that));
	}
	public Real add(Real that)
	{
		if (that.isSpecial())
			return that.add(this);
		return new Real(delegate.add(that.delegate));
	}


	public Real sub(long that)
	{
		return sub(BMath.valueOf(that));
	}
	public Real sub(String that)
	{
		return sub(BMath.valueOf(that));
	}
	public Real sub(Real that)
	{
		if (that.isSpecial())
			return that.sub(this).neg();
		return new Real(delegate.subtract(that.delegate));
	}


	public Real mul(long that)
	{
		return mul(BMath.valueOf(that));
	}
	public Real mul(String that)
	{
		return mul(BMath.valueOf(that));
	}
	public Real mul(Real that)
	{
		if (that.isSpecial())
			return that.mul(this);
		return new Real(delegate.multiply(that.delegate));
	}


	public Real div(long that)
	{
		return div(BMath.valueOf(that));
	}
	public Real div(String that)
	{
		return div(BMath.valueOf(that));
	}
	public Real div(Real that)
	{
		if (that.isSpecial())
			return that.div(this).inv();
		if (that.equals(BMath.ZERO))
			return BMath.INF;
		return new Real(delegate.divide(that.delegate));
	}


	public Real pow(long that)
	{
		return new Real(ApfloatMath.pow(delegate, that));
	}
	public Real pow(String that)
	{
		return pow(BMath.valueOf(that));
	}
	public Real pow(Real that)
	{
		if (that.isSpecial())
			return BMath.NAN;
		return new Real(ApfloatMath.pow(delegate, that.delegate));
	}


	public Real min(long that)
	{
		return min(BMath.valueOf(that));
	}
	public Real min(String that)
	{
		return min(BMath.valueOf(that));
	}
	public Real min(Real that)
	{
		if (that.isSpecial())
			return that.min(this);
		return compareTo(that) <= 0 ? this : that;
	}


	public Real max(long that)
	{
		return max(BMath.valueOf(that));
	}
	public Real max(String that)
	{
		return max(BMath.valueOf(that));
	}
	public Real max(Real that)
	{
		if (that.isSpecial())
			return that.max(this);
		return compareTo(that) >= 0 ? this : that;
	}


	@Override
	public boolean equals(Object that)
	{
		return (this == that)
					 || (that != null && getClass().isInstance(that) && equals((Real) that));
	}
	public boolean equals(long that)
	{
		return equals(BMath.valueOf(that));
	}
	public boolean equals(String that)
	{
		return equals(BMath.valueOf(that));
	}
	public boolean equals(Real that)
	{
		if (that.isSpecial())
			return that.equals(this);
		return delegate.equals(that.delegate);
	}


	public int compareTo(long that)
	{
		return compareTo(BMath.valueOf(that));
	}
	public int compareTo(String that)
	{
		return compareTo(BMath.valueOf(that));
	}
	@Override
	public int compareTo(@NotNull Real that)
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
		if (equals(BMath.ZERO))
			return BMath.INF;
		return new Real(new Apint(1).divide(delegate));
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


	public long scale()
	{
		return delegate.scale();
	}


	public long precision()
	{
		if (delegate.precision() == Long.MAX_VALUE)
			return Long.MAX_VALUE;
		return delegate.precision() - delegate.scale();
	}


	public Real precision(long precision)
	{
		Rational x = BMath.TEN.pow(precision);
		Real rounded = mul(x).round().div(x);
		if (rounded.delegate.equals(Apint.ZERO)) return BMath.ZERO;
		return new Real(rounded.delegate.precision(precision + rounded.delegate.scale()));
	}

	@Override
	public String toString()
	{
		return delegate.toString(true);
	}
}
