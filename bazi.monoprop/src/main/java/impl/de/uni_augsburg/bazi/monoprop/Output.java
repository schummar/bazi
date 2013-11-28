package de.uni_augsburg.bazi.monoprop;

import java.util.List;

import com.google.common.collect.ImmutableList;

import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Rational;

class Output implements MonopropMethod.Output
{
	public Int seats;
	public List<? extends Party> parties;

	public Output()
	{
		this.seats = BMath.ZERO;
		this.parties = ImmutableList.of();
	}
	public Output(Int seats, List<? extends Party> parties)
	{
		this.seats = seats;
		this.parties = parties;
	}
	@Override public Int getSeats()
	{
		return seats;
	}
	@Override public ImmutableList<? extends Party> getParties()
	{
		return ImmutableList.copyOf(parties);
	}


	public static class Party implements MonopropMethod.Output.Party
	{
		public String name;
		public Rational votes;
		public Int min, max, dir;
		public Rational quotient;
		public Int seats;
		public Uniqueness uniqueness;

		public Party()
		{
			this.name = "";
			this.votes = BMath.ZERO;
			this.min = BMath.ZERO;
			this.max = BMath.ZERO;
			this.dir = BMath.ZERO;
			this.quotient = BMath.ZERO;
			this.seats = BMath.ZERO;
			this.uniqueness = Uniqueness.UNIQUE;
		}
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
