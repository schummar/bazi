package de.uni_augsburg.bazi.divisor;

import de.uni_augsburg.bazi.common_vector.ShiftQueue;
import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Rational;
import de.uni_augsburg.bazi.math.Real;

import java.util.HashMap;
import java.util.Map;

public interface RoundingFunction
{
	Real getBorder(Int value, long minPrecision);
	Real[] getBorders(Real value, long minPrecision);
	Int round(Real value, long minPrecision);
	Rational getParam();
	boolean isImpervious();

	public default ShiftQueue.ShiftFunction getShiftFunction(long minPrecision)
	{
		return (party, seats, increase) -> {
			Real border = getBorder(seats, minPrecision);
			if (border.sgn() <= 0)
				return BMath.INF;
			return party.votes().div(border);
		};
	}


	public static final Stationary DIV_STD = new Stationary(BMath.valueOf("0.5"), null);
	public static final Stationary DIV_DWN = new Stationary(BMath.valueOf(1), null);
	public static final Stationary DIV_UPW = new Stationary(BMath.valueOf(0), null);
	public static final Geometric DIV_GEO = new Geometric();
	public static final Harmonic DIV_HAR = new Harmonic();


	// //////////////////////////////////////////////////////////////////////////

	public static interface ExactRoundingFunction extends RoundingFunction
	{
		@Override
		public default Rational getBorder(Int value, long minPrecision)
		{
			return getBorder(value);
		}
		public Rational getBorder(Int value);
		@Override
		public default Rational[] getBorders(Real value, long minPrecision)
		{
			return getBorders(value);
		}
		public Rational[] getBorders(Real value);
		@Override
		public default Int round(Real value, long minPrecision)
		{
			return round(value);
		}
		public Int round(Real value);
	}


	public static class Stationary implements ExactRoundingFunction
	{
		private final Rational r;
		private final Map<Int, Rational> specialCases;

		public Stationary(Rational r, Map<Int, Rational> specialCases)
		{
			this.r = r;
			this.specialCases = specialCases != null ? new HashMap<>(specialCases) : new HashMap<>();
		}

		@Override
		public Rational getBorder(Int value)
		{
			Rational r = specialCases.get(value);
			if (r == null)
				r = this.r;
			return value.add(r);
		}

		@Override
		public Rational[] getBorders(Real value)
		{
			Rational hi = getBorder(value.floor());
			Rational lo = hi;
			if (!value.equals(hi))
				lo = getBorder(value.floor().sub(1));
			return new Rational[]{lo, hi};
		}

		@Override
		public Int round(Real value)
		{
			if (value.compareTo(getBorder(value.floor())) > 0)
				return value.ceil();
			return value.floor();
		}

		@Override
		public Rational getParam()
		{
			return r;
		}

		@Override public boolean isImpervious()
		{

			return r.equals(BMath.ZERO) || (specialCases.containsKey(BMath.ZERO) && specialCases.get(BMath.ZERO).equals(BMath.ZERO));
		}
	}

	// //////////////////////////////////////////////////////////////////////////


	public static class Power implements RoundingFunction
	{
		private final Rational p;
		private final Map<Int, Rational> specialCases;

		public Power(Rational p, Map<Int, Rational> specialCases)
		{
			this.p = p;
			this.specialCases = specialCases != null ? new HashMap<>(specialCases) : new HashMap<>();
		}

		@Override
		public Real getBorder(Int value, long minPrecision)
		{
			Rational p = specialCases.get(value);
			if (p == null)
				p = this.p;

			if (p.equals(BMath.ZERO)) return calcGeometric(value, minPrecision);
			if (p.equals(BMath.MINUS_ONE)) return calcHarmonic(value);
			return calc(value, p, minPrecision);
		}

		protected Real calc(Int value, Rational p, long minPrecision)
		{
			long precision = minPrecision;
			while (true)
			{
				Real x = value.precision(precision);
				Real res = x.pow(p).add(x.add(1).pow(p)).div(2).pow(p.neg());
				if (res.precision() >= minPrecision) return res;
				precision *= 2;
			}
		}

		protected Real calcGeometric(Int value, long minPrecision)
		{
			if (value.equals(BMath.ZERO)) return BMath.ZERO;
			return BMath.pow(value.mul(value.add(1)), BMath.HALF, minPrecision);
		}

		protected Rational calcHarmonic(Int value)
		{
			if (value.equals(BMath.ZERO)) return BMath.ZERO;
			return value.inv().add(value.add(1).inv()).div(2).inv();
		}


		@Override
		public Real[] getBorders(Real value, long minPrecision)
		{
			Real hi = getBorder(value.floor(), minPrecision);
			Real lo = hi;
			if (!value.equals(hi))
				lo = getBorder(value.floor().sub(1), minPrecision);
			return new Real[]{lo, hi};
		}

		@Override
		public Int round(Real value, long minPrecision)
		{
			if (value.compareTo(getBorder(value.floor(), minPrecision)) > 0)
				return value.ceil();
			return value.floor();
		}

		@Override
		public Rational getParam()
		{
			return p;
		}

		@Override public boolean isImpervious() { return false; }
	}


	// //////////////////////////////////////////////////////////////////////////


	public static class Geometric extends Power
	{
		public Geometric() { super(BMath.ZERO, null); }

		@Override public boolean isImpervious() { return true; }
	}


	// //////////////////////////////////////////////////////////////////////////


	public static class Harmonic extends Power implements ExactRoundingFunction
	{
		public Harmonic()
		{
			super(BMath.MINUS_ONE, null);
		}

		@Override
		public Rational getBorder(Int value, long minPrecision)
		{
			return getBorder(value);
		}
		@Override
		public Rational[] getBorders(Real value, long minPrecision)
		{
			return getBorders(value);
		}
		@Override
		public Int round(Real value, long minPrecision)
		{
			return round(value);
		}

		@Override
		public Rational getBorder(Int value)
		{
			return value.inv().add(value.add(1).inv()).div(2).inv();
		}

		@Override
		public Rational[] getBorders(Real value)
		{
			Rational hi = getBorder(value.floor());
			Rational lo = hi;
			if (!value.equals(hi))
				lo = getBorder(value.floor().sub(1));
			return new Rational[]{lo, hi};
		}

		@Override
		public Int round(Real value)
		{
			if (value.compareTo(getBorder(value.floor())) > 0)
				return value.ceil();
			return value.floor();
		}

		@Override public boolean isImpervious() { return true; }
	}
}
