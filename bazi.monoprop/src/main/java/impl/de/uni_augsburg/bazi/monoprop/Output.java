package de.uni_augsburg.bazi.monoprop;

import com.google.common.collect.ImmutableList;

import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Rational;
import de.uni_augsburg.bazi.monoprop.MonopropMethod;
import de.uni_augsburg.bazi.monoprop.Uniqueness;
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
		private final Input.Party iParty;
		private final Rational quotient;
		private final Int seats;
		private final Uniqueness uniqueness;

		public Party(de.uni_augsburg.bazi.monoprop.MonopropMethod.Input.Party iParty, Rational quotient, Int seats, Uniqueness uniqueness)
		{
			this.iParty = iParty;
			this.quotient = quotient;
			this.seats = seats;
			this.uniqueness = uniqueness;
		}

		@Override public String getName()
		{
			return iParty.getName();
		}
		@Override public Rational getVotes()
		{
			return iParty.getVotes();
		}
		@Override public Int getMin()
		{
			return iParty.getMin();
		}
		@Override public Int getMax()
		{
			return iParty.getMax();
		}
		@Override public Int getDir()
		{
			return iParty.getDir();
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
