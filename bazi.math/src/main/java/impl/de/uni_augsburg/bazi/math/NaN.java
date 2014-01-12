package de.uni_augsburg.bazi.math;

import java.util.List;
import java.util.function.UnaryOperator;

import de.uni_augsburg.bazi.common.Json.SerializeAsString;

@SerializeAsString class NaN extends Int
{
	public NaN()
	{
		super(0);
	}


	@Override public Int add(Int that)
	{
		return this;
	}
	@Override public Int add(long that)
	{
		return this;
	}
	@Override public Rational add(Rational that)
	{
		return this;
	}
	@Override public Real add(Real that)
	{
		return this;
	}
	@Override public Rational add(String that)
	{
		return this;
	}


	@Override public Int sub(Int that)
	{
		return this;
	}
	@Override public Int sub(long that)
	{
		return this;
	}
	@Override public Rational sub(Rational that)
	{
		return this;
	}
	@Override public Real sub(Real that)
	{
		return this;
	}
	@Override public Rational sub(String that)
	{
		return this;
	}


	@Override public Rational div(long that)
	{
		return this;
	}
	@Override public Rational div(Rational that)
	{
		return this;
	}
	@Override public Real div(Real that)
	{
		return this;
	}
	@Override public Rational div(String that)
	{
		return this;
	}


	@Override public Int mul(Int that)
	{
		return this;
	}
	@Override public Int mul(long that)
	{
		return this;
	}
	@Override public Rational mul(Rational that)
	{
		return this;
	}
	@Override public Real mul(Real that)
	{
		return this;
	}
	@Override public Rational mul(String that)
	{
		return this;
	}


	@Override public Rational pow(long that)
	{
		return this;
	}
	@Override public Real pow(Real that)
	{
		return this;
	}
	@Override public Real pow(String that)
	{
		return this;
	}


	@Override public Int min(Int that)
	{
		return this;
	}
	@Override public Int min(long that)
	{
		return this;
	}
	@Override public Rational min(Rational that)
	{
		return this;
	}
	@Override public Real min(Real that)
	{
		return this;
	}
	@Override public Rational min(String that)
	{
		return this;
	}


	@Override public Int max(Int that)
	{
		return this;
	}
	@Override public Int max(long that)
	{
		return this;
	}
	@Override public Rational max(Rational that)
	{
		return this;
	}
	@Override public Real max(Real that)
	{
		return this;
	}
	@Override public Rational max(String that)
	{
		return this;
	}


	@Override public boolean equals(Int that)
	{
		return that instanceof NaN;
	}
	@Override public boolean equals(long that)
	{
		return false;
	}
	@Override public boolean equals(Rational that)
	{
		return that instanceof NaN;
	}
	@Override public boolean equals(Real that)
	{
		return that instanceof NaN;
	}
	@Override public boolean equals(String that)
	{
		return equals(BMath.valueOf(that));
	}


	@Override public int compareTo(Int that)
	{
		throw new RuntimeException("cant compare to NaN");
	}
	@Override public int compareTo(long that)
	{
		throw new RuntimeException("cant compare to NaN");
	}
	@Override public int compareTo(Rational that)
	{
		throw new RuntimeException("cant compare to NaN");
	}
	@Override public int compareTo(Real that)
	{
		throw new RuntimeException("cant compare to NaN");
	}
	@Override public int compareTo(String that)
	{
		throw new RuntimeException("cant compare to NaN");
	}


	@Override public Int ceil()
	{
		return this;
	}


	@Override public Int floor()
	{
		return this;
	}


	@Override public Rational frac()
	{
		return this;
	}


	@Override public Rational inv()
	{
		return this;
	}


	@Override public boolean isSpecial()
	{
		return true;
	}


	@Override public Int neg()
	{
		return this;
	}


	@Override public Int round()
	{
		return this;
	}


	@Override public int sgn()
	{
		throw new RuntimeException("NaN has no sign");
	}


	@Override public de.uni_augsburg.bazi.math.Int Int()
	{
		throw new RuntimeException("'NaN.Int' not yet implemented");
	}


	@Override public List<Int> countTo()
	{
		throw new RuntimeException("cannot count to NaN");
	}
	@Override public List<Int> countTo(Int to)
	{
		throw new RuntimeException("cannot count from NaN");
	}


	@Override public void timesDo(Runnable r)
	{
		throw new RuntimeException("cannot count to NaN");
	}
	@Override public void timesDo(UnaryOperator<de.uni_augsburg.bazi.math.Int> op)
	{
		throw new RuntimeException("cannot count to NaN");
	}


	@Override public String toString()
	{
		return BMath.NAN_STRING;
	}
}
