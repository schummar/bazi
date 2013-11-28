package de.uni_augsburg.bazi.monoprop;

import com.google.common.collect.ImmutableList;

import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Rational;

class Output extends Input implements MonopropMethod.Output
{
	private final ImmutableList<? extends Party> parties;

	public Output(Int seats, ImmutableList<? extends Party> parties)
	{
		super(seats, parties);
		this.parties = parties;
	}
	@Override public ImmutableList<? extends Party> getParties()
	{
		return parties;
	}


	public static class Party extends Input.Party implements MonopropMethod.Output.Party
	{
		private final Rational quotient;
		private final Int seats;
		private final Uniqueness uniqueness;

		public Party(String name, Rational votes, Int min, Int max, Int dir, Rational quotient, Int seats, Uniqueness uniqueness)
		{
			super(name, votes, min, max, dir);
			this.quotient = quotient;
			this.seats = seats;
			this.uniqueness = uniqueness;
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
