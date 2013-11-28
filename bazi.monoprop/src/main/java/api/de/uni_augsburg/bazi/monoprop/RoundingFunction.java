package de.uni_augsburg.bazi.monoprop;

import com.google.common.collect.ImmutableMap;

import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Rational;
import de.uni_augsburg.bazi.math.Real;

public interface RoundingFunction
{
	public Real getBorder(Int value);
	public Real[] getBorders(Real value);


	public static final RoundingFunction
			DIV_STD = new Stationary(BMath.rationalOf("0.5"), null),
			DIV_DWD = new Stationary(BMath.intOf(1), null),
			DIV_UPW = new Stationary(BMath.intOf(0), null),
			DIV_GEO = new Geometric(),
			DIV_HAR = new Harmonic();


	// //////////////////////////////////////////////////////////////////////////


	public static class Stationary implements RoundingFunction
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

		@Override public Real getBorder(Int value)
		{
			Rational p = specialCases.get(value);
			if (p == null)
				p = this.p;
			return value.pow(p).add(value.add(1).pow(p)).div(2).pow(p.neg());
		}

		@Override public Real[] getBorders(Real value)
		{
			Real hi = getBorder(value.floor());
			Real lo = hi;
			if (!value.equals(hi))
				lo = getBorder(value.floor().sub(1));
			return new Real[] { lo, hi };
		}
	}


	// //////////////////////////////////////////////////////////////////////////


	public static class Geometric implements RoundingFunction
	{
		@Override public Real getBorder(Int value)
		{
			return value.mul(value.add(1)).pow(BMath.HALF);
		}

		@Override public Real[] getBorders(Real value)
		{
			Real hi = getBorder(value.floor());
			Real lo = hi;
			if (!value.equals(hi))
				lo = getBorder(value.floor().sub(1));
			return new Real[] { lo, hi };
		}
	}


	// //////////////////////////////////////////////////////////////////////////


	public static class Harmonic implements RoundingFunction
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
	}
}
