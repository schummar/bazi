package de.uni_augsburg.bazi.monoprop;

import java.util.List;

import com.google.common.collect.ImmutableList;

import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Rational;

public interface MonopropMethod
{
	public Output calculate(Input input);


	// ////////////////////////////////////////////////////////////////////////

	public interface Input
	{
		public Int getSeats();
		public ImmutableList<? extends Party> getParties();

		public interface Party
		{
			public String getName();
			public Rational getVotes();
			public Int getMin();
			public Int getMax();
			public Int getDir();
		}
	}


	// ////////////////////////////////////////////////////////////////////////


	class Output implements Input
	{
		public final Int seats;
		public final List<? extends Party> parties;

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


		public static class Party implements Input.Party
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
			public Rational getQuotient()
			{
				return quotient;
			}
			public Int getSeats()
			{
				return seats;
			}
			public Uniqueness getUniqueness()
			{
				return uniqueness;
			}
		}
	}
}
