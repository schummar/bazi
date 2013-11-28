package de.uni_augsburg.bazi.monoprop;

import com.google.common.collect.ImmutableList;

import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Rational;
import de.uni_augsburg.bazi.monoprop.MonopropMethod.Input;

class Output implements MonopropMethod.Output
{
	private final Int seats;
	private final ImmutableList<? extends Party> parties;

	public Output(Int seats, ImmutableList<? extends Party> parties)
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

	public static class Party implements MonopropMethod.Output.Party
	{
		private final Input.Party inputParty;
		private final Rational quotient;
		private final Int seats;
		private final Uniqueness uniqueness;

		public Party(MonopropMethod.Input.Party inputParty, Rational quotient, Int seats, Uniqueness uniqueness)
		{
			this.inputParty = inputParty;
			this.quotient = quotient;
			this.seats = seats;
			this.uniqueness = uniqueness;
		}

		@Override public String getName()
		{
			return inputParty.getName();
		}
		@Override public Rational getVotes()
		{
			return inputParty.getVotes();
		}
		@Override public Int getMin()
		{
			return inputParty.getMin();
		}
		@Override public Int getMax()
		{
			return inputParty.getMax();
		}
		@Override public Int getDir()
		{
			return inputParty.getDir();
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
