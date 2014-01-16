package de.uni_augsburg.bazi.divisor;

import com.google.common.collect.ImmutableMap;

import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Rational;
import de.uni_augsburg.bazi.math.Real;

public interface RoundingFunction
{
	public Real getBorder(Int value, int minPrecision);
	public Real[] getBorders(Real value, int minPrecision);
	public Int round(Real value, int minPrecision);


	public static final Stationary DIV_STD = new Stationary(BMath.valueOf("0.5"), null);
	public static final Stationary DIV_DWD = new Stationary(BMath.valueOf(1), null);
	public static final Stationary DIV_UPW = new Stationary(BMath.valueOf(0), null);
	public static final Geometric DIV_GEO = new Geometric();
	public static final Harmonic DIV_HAR = new Harmonic();


	// //////////////////////////////////////////////////////////////////////////

	public static interface ExactRoundingFunction extends RoundingFunction
	{
		@Override public default Rational getBorder(Int value, int minPrecision)
		{
			return getBorder(value);
		}
		public Rational getBorder(Int value);
		@Override public default Rational[] getBorders(Real value, int minPrecision)
		{
			return getBorders(value);
		}
		public Rational[] getBorders(Real value);
		@Override public default Int round(Real value, int minPrecision)
		{
			return round(value);
		}
		public Int round(Real value);
	}

	public static class Stationary implements ExactRoundingFunction
	{
		private final Rational r;
		private final ImmutableMap<Int, Rational> specialCases;

		public Stationary(Rational r, ImmutableMap<Int, Rational> specialCases)
		{
			this.r = r;
			this.specialCases = specialCases != null ? specialCases : ImmutableMap.<Int, Rational> of();
		}

		@Override public Rational getBorder(Int value)
		{
			Rational r = specialCases.get(value);
			if (r == null)
				r = this.r;
			return value.add(r);
		}

		@Override public Rational[] getBorders(Real value)
		{
			Rational hi = getBorder(value.floor());
			Rational lo = hi;
			if (!value.equals(hi))
				lo = getBorder(value.floor().sub(1));
			return new Rational[] { lo, hi };
		}

		@Override public Int round(Real value)
		{
			if (value.compareTo(getBorder(value.floor())) > 0)
				return value.ceil();
			return value.floor();
		}

		public Rational getParam()
		{
			return r;
		}
	}

	// //////////////////////////////////////////////////////////////////////////


	public static class Power implements RoundingFunction
	{
		private final Rational p;
		private final ImmutableMap<Int, Rational> specialCases;

		public Power(Rational p, ImmutableMap<Int, Rational> specialCases)
		{
			this.p = p;
			this.specialCases = specialCases != null ? specialCases : ImmutableMap.<Int, Rational> of();
		}

		@Override public Real getBorder(Int value, int minPrecision)
		{
			Rational p = specialCases.get(value);
			if (p == null)
				p = this.p;
			Real approx = value.precision(minPrecision);
			return approx.pow(p).add(approx.add(1).pow(p)).div(2).pow(p.neg());
		}

		@Override public Real[] getBorders(Real value, int minPrecision)
		{
			Real hi = getBorder(value.floor(), minPrecision);
			Real lo = hi;
			if (!value.equals(hi))
				lo = getBorder(value.floor().sub(1), minPrecision);
			return new Real[] { lo, hi };
		}

		@Override public Int round(Real value, int minPrecision)
		{
			if (value.compareTo(getBorder(value.floor(), minPrecision)) > 0)
				return value.ceil();
			return value.floor();
		}
	}


	// //////////////////////////////////////////////////////////////////////////


	public static class Geometric implements RoundingFunction
	{
		@Override public Real getBorder(Int value, int minPrecision)
		{
			return value.mul(value.add(1)).precision(minPrecision).pow(BMath.HALF);
		}

		@Override public Real[] getBorders(Real value, int minPrecision)
		{
			Real hi = getBorder(value.floor(), minPrecision);
			Real lo = hi;
			if (!value.equals(hi))
				lo = getBorder(value.floor().sub(1), minPrecision);
			return new Real[] { lo, hi };
		}

		@Override public Int round(Real value, int minPrecision)
		{
			if (value.compareTo(getBorder(value.floor(), minPrecision)) > 0)
				return value.ceil();
			return value.floor();
		}
	}


	// //////////////////////////////////////////////////////////////////////////


	public static class Harmonic implements ExactRoundingFunction
	{
		@Override public Rational getBorder(Int value)
		{
			return value.inv().add(value.add(1).inv()).div(2).inv();
		}

		@Override public Rational[] getBorders(Real value)
		{
			Rational hi = getBorder(value.floor());
			Rational lo = hi;
			if (!value.equals(hi))
				lo = getBorder(value.floor().sub(1));
			return new Rational[] { lo, hi };
		}

		@Override public Int round(Real value)
		{
			if (value.compareTo(getBorder(value.floor())) > 0)
				return value.ceil();
			return value.floor();
		}
	}
}
