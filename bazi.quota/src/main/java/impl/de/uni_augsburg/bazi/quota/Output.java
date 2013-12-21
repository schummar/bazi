package de.uni_augsburg.bazi.quota;

import java.util.List;

import com.google.common.collect.ImmutableList;

import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Rational;
import de.uni_augsburg.bazi.monoprop.MonopropMethod;
import de.uni_augsburg.bazi.monoprop.Uniqueness;


class Output implements QuotaMethod.Output
{
	public final Int seats;
	public final List<? extends Party> parties;
	public Rational quota;

	public Output(Int seats, List<? extends Party> parties, Rational quota)
	{
		this.seats = seats;
		this.parties = parties;
		this.quota = quota;
	}
	@Override public Int getSeats()
	{
		return seats;
	}
	@Override public ImmutableList<? extends Party> getParties()
	{
		return ImmutableList.copyOf(parties);
	}
	@Override public Rational getQuota()
	{
		return quota;
	}

	public static class Party implements MonopropMethod.Output.Party
	{
		public final String name;
		public final Rational votes;
		public final Int min, max, dir;
		public final Rational quotient;
		public final Int seats;
		public final Uniqueness uniqueness;

		public Party(String name, Rational votes, Int min, Int max, Int dir, Rational quotient, Int seats, Uniqueness uniqueness)
		{
			this.name = name;
			this.votes = votes;
			this.min = min;
			this.max = max;
			this.dir = dir;
			this.quotient = quotient;
			this.seats = seats;
			this.uniqueness = uniqueness;
		}
		@Override public String getName()
		{
			return name;
		}
		@Override public Rational getVotes()
		{
			return votes;
		}
		@Override public Int getMin()
		{
			return min;
		}
		@Override public Int getMax()
		{
			return max;
		}
		@Override public Int getDir()
		{
			return dir;
		}
		@Override public Rational getQuotient()
		{
			return quotient;
		}
		@Override public Int getSeats()
		{
			return seats;
		}
		@Override public Uniqueness getUniqueness()
		{
			return uniqueness;
		}
	}
}