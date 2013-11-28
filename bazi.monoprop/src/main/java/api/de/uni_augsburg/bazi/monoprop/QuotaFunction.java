package de.uni_augsburg.bazi.monoprop;

import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Rational;

public interface QuotaFunction
{
	public Rational getQuota(Rational votes, Int seats);


	public static final QuotaFunction
			HARE = new Hare(),
			HARE_VAR1 = new Hare_Var1(),
			HARE_VAR2 = new Hare_Var2(),
			DROOP = new Droop(),
			DROOP_VAR1 = new Droop_Var1(),
			DROOP_VAR2 = new Droop_Var2(),
			DROOP_VAR3 = new Droop_Var3();


	// //////////////////////////////////////////////////////////////////////////


	public static class Hare implements QuotaFunction
	{
		@Override public Rational getQuota(Rational votes, Int seats)
		{
			return votes.div(seats);
		}
	}

	public static class Hare_Var1 implements QuotaFunction
	{
		@Override public Rational getQuota(Rational votes, Int seats)
		{
			return votes.div(seats).floor();
		}
	}

	public static class Hare_Var2 implements QuotaFunction
	{
		@Override public Rational getQuota(Rational votes, Int seats)
		{
			return votes.div(seats).ceil();
		}
	}

	public static class Droop implements QuotaFunction
	{
		@Override public Rational getQuota(Rational votes, Int seats)
		{
			return votes.div(seats.add(1)).floor().add(1);
		}
	}

	public static class Droop_Var1 implements QuotaFunction
	{
		@Override public Rational getQuota(Rational votes, Int seats)
		{
			return votes.div(seats.add(1)).floor().max(1);
		}
	}

	public static class Droop_Var2 implements QuotaFunction
	{
		@Override public Rational getQuota(Rational votes, Int seats)
		{
			return votes.div(seats.add(1)).ceil();
		}
	}

	public static class Droop_Var3 implements QuotaFunction
	{
		@Override public Rational getQuota(Rational votes, Int seats)
		{
			return votes.div(seats.add(1));
		}
	}

}
