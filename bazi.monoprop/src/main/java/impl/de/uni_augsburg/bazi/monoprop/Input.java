package de.uni_augsburg.bazi.monoprop;

import com.google.common.collect.ImmutableList;

import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Rational;

class Input implements MonopropMethod.Input
{
	private final Int seats;
	private final ImmutableList<? extends Party> parties;

	public Input(Int seats, ImmutableList<? extends Party> parties)
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
		return parties;
	}


	public static class Party implements MonopropMethod.Input.Party
	{
		private final String name;
		private final Rational votes;
		private final Int min, max, dir;

		public Party(String name, Rational votes, Int min, Int max, Int dir)
		{
			this.name = name;
			this.votes = votes;
			this.min = min;
			this.max = max;
			this.dir = dir;
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
	}
}
