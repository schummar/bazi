package de.uni_augsburg.bazi.monoprop;

import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Rational;
import de.uni_augsburg.bazi.monoprop.QuotaMethod.QuotaFunction;

public enum QuotaFunctions implements QuotaFunction
{
	HARE
	{
		@Override public Rational getQuota(Rational votes, Int seats)
		{
			return votes.div(seats);
		}
	},

	HARE_VAR1
	{
		@Override public Rational getQuota(Rational votes, Int seats)
		{
			return votes.div(seats).floor();
		}
	},

	HARE_VAR2
	{
		@Override public Rational getQuota(Rational votes, Int seats)
		{
			return votes.div(seats).ceil();
		}
	},

	DROOP
	{
		@Override public Rational getQuota(Rational votes, Int seats)
		{
			return votes.div(seats.add(1)).floor().add(1);
		}
	},

	DROOP_VAR1
	{
		@Override public Rational getQuota(Rational votes, Int seats)
		{
			return votes.div(seats.add(1)).floor().max(1);
		}
	},

	DROOP_VAR2
	{
		@Override public Rational getQuota(Rational votes, Int seats)
		{
			return votes.div(seats.add(1)).ceil();
		}
	},

	DROOP_VAR3
	{
		@Override public Rational getQuota(Rational votes, Int seats)
		{
			return votes.div(seats.add(1));
		}
	};

}
