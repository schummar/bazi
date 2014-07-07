package de.uni_augsburg.bazi.divisor;

import de.uni_augsburg.bazi.common_vector.ShiftQueue;
import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Rational;
import de.uni_augsburg.bazi.math.Real;

import java.util.HashMap;
import java.util.Map;

/** A rounding function defines for each integer i a border r(i). All real values in [r(i-1),r(i)] can be rounded to i. */
public interface RoundingFunction
{
	/**
	 * Calculate the rounding border for the given value.
	 * @param value the value.
	 * @param minPrecision if the border is non-rational, to how many decimal places should the border be calculated?
	 * @return the border.
	 */
	Real apply(Int value, long minPrecision);


	/**
	 * Rounds the given value according to this roundinf function.
	 * Please note that there are cases where rounding is not unique. In those cases the value will be rounded upwards.
	 * @param value the value.
	 * @param minPrecision if the borders are non-rational, to how many decimal places should the border be calculated?
	 * @return the rounded value.
	 */
	default Int round(Real value, long minPrecision)
	{
		return value.compareTo(apply(value.floor(), minPrecision)) < 0
			? value.floor()
			: value.ceil();
	}


	/**
	 * Returns the parameter (p for power, r for stationary rounding).
	 * @return the parameter (p for power, r for stationary rounding).
	 */
	Rational getParam();


	/**
	 * Whether the rounding function is impervious (r(0)==0).
	 * @return true iff the rounding function is impervious (r(0)==0).
	 */
	boolean isImpervious();


	/**
	 * Returns a shift function for this rounding function.
	 * @param minPrecision if the borders are non-rational, to how many decimal places should the border be calculated?
	 * @return a shift function for this rounding function.
	 */
	default ShiftQueue.ShiftFunction getShiftFunction(long minPrecision)
	{
		return (party, seats) -> {
			Real border = apply(seats, minPrecision);
			if (border.sgn() <= 0)
				return BMath.INF;
			return party.votes().div(border);
		};
	}

	/** Stationary rounding function (r=0.5). */
	public static final Stationary DIV_STD = new Stationary(BMath.valueOf("0.5"), null);
	/** Stationary rounding function (r=1). */
	public static final Stationary DIV_DWN = new Stationary(BMath.valueOf(1), null);
	/** Stationary rounding function (r=0). */
	public static final Stationary DIV_UPW = new Stationary(BMath.valueOf(0), null);
	/** Geometric rounding: Power rounding function (p=0.5). */
	public static final Geometric DIV_GEO = new Geometric();
	/** Harmonic rounding: Power rounding function (p=-1). */
	public static final Harmonic DIV_HAR = new Harmonic();


	// //////////////////////////////////////////////////////////////////////////

	/** An exact rounding function delivers rational values for all borders. */
	public static interface ExactRoundingFunction extends RoundingFunction
	{
		@Override default Rational apply(Int value, long minPrecision)
		{
			return apply(value);
		}

		/**
		 * Calculate the rounding border for the given value.
		 * @param value the value.
		 * @return the border.
		 */
		Rational apply(Int value);

		/**
		 * Rounds the given value according to this roundinf function.
		 * Please note that there are cases where rounding is not unique. In those cases the value will be rounded upwards.
		 * @param value the value.
		 * @return the rounded value.
		 */
		default Int round(Real value)
		{
			return value.compareTo(apply(value.floor())) < 0
				? value.floor()
				: value.ceil();
		}


		@Override default boolean isImpervious()
		{
			return apply(BMath.ZERO).equals(BMath.ZERO);
		}
	}

	/** A stationary rounding function. */
	public static class Stationary implements ExactRoundingFunction
	{
		private final Rational r;
		private final Map<Int, Rational> specialCases;

		/**
		 * @param r the parameter that determines the rounding borders as i+r.
		 * @param specialCases special cases that replace r for i if specialCases.containsKey(i).
		 */
		public Stationary(Rational r, Map<Int, Rational> specialCases)
		{
			this.r = r;
			this.specialCases = specialCases != null ? new HashMap<>(specialCases) : new HashMap<>();
		}

		@Override
		public Rational apply(Int value)
		{
			Rational r = specialCases.get(value);
			if (r == null)
				r = this.r;
			return value.add(r);
		}

		@Override
		public Rational getParam()
		{
			return r;
		}
	}

	// //////////////////////////////////////////////////////////////////////////

	/** A power rounding function. */
	public static class Power implements RoundingFunction
	{
		private final Rational p;
		private final Map<Int, Rational> specialCases;

		/**
		 * @param p the parameter that determines the rounding borders as ((i^p+(i+1)^p)/2)^(1/p)
		 * @param specialCases special cases that replace p for i if specialCases.containsKey(i).
		 */
		public Power(Rational p, Map<Int, Rational> specialCases)
		{
			this.p = p;
			this.specialCases = specialCases != null ? new HashMap<>(specialCases) : new HashMap<>();

			checkParam(this.p);
			this.specialCases.values().forEach(Power::checkParam);
		}

		private static void checkParam(Rational p)
		{
			if (p == null || p.compareTo(0) < 0 && !p.equals(-1))
				throw new RuntimeException("Invalid p: " + p);
		}

		@Override
		public Real apply(Int value, long minPrecision)
		{
			Rational p = specialCases.getOrDefault(BMath.ZERO, this.p);
			if (p.equals(BMath.ZERO)) return geometricMean(value, minPrecision);
			if (p.equals(BMath.MINUS_ONE)) return harmonicMean(value);
			return powerMean(value, p, minPrecision);
		}

		@Override
		public Rational getParam()
		{
			return p;
		}

		@Override public boolean isImpervious()
		{
			Rational p = specialCases.getOrDefault(BMath.ZERO, this.p);
			return p.equals(BMath.ZERO) || p.equals(BMath.MINUS_ONE);
		}
	}


	// //////////////////////////////////////////////////////////////////////////

	/** A geometric rounding function. */
	public static class Geometric extends Power
	{
		public Geometric() { super(BMath.ZERO, null); }
	}


	// //////////////////////////////////////////////////////////////////////////

	/** A harmonic rounding function. */
	public static class Harmonic extends Power implements ExactRoundingFunction
	{
		public Harmonic()
		{
			super(BMath.MINUS_ONE, null);
		}

		@Override
		public Rational apply(Int value, long minPrecision)
		{
			return apply(value);
		}

		@Override
		public Rational apply(Int value)
		{
			return harmonicMean(value);
		}
	}


	// //////////////////////////////////////////////////////////////////////////


	/**
	 * Calculate the power mean between value and value+1.
	 * @param value the value.
	 * @param p the power.
	 * @param minPrecision to how many decimal places should the border be calculated?
	 * @return the power mean.
	 */
	public static Real powerMean(Int value, Rational p, long minPrecision)
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


	/**
	 * Calculate the geometric mean between value and value+1.
	 * @param value the value.
	 * @param minPrecision to how many decimal places should the border be calculated?
	 * @return the geometric mean.
	 */
	public static Real geometricMean(Int value, long minPrecision)
	{
		if (value.equals(BMath.ZERO)) return BMath.ZERO;
		return BMath.pow(value.mul(value.add(1)), BMath.HALF, minPrecision);
	}


	/**
	 * Calculate the harmonic mean between value and value+1.
	 * @param value the value.
	 * @return the harmonic mean.
	 */
	public static Rational harmonicMean(Int value)
	{
		if (value.equals(BMath.ZERO)) return BMath.ZERO;
		return BMath.TWO.div(value.inv().add(value.add(1).inv()));
	}
}
