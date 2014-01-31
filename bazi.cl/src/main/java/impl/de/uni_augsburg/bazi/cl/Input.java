package de.uni_augsburg.bazi.cl;

import com.google.common.collect.ImmutableList;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Rational;
import de.uni_augsburg.bazi.monoprop.MonopropInput;

import java.util.List;

class Input implements MonopropInput
{
	boolean validate()
	{
		return true;
	}


	private final Int seats;
	private final List<? extends Party> parties;

	public Input(Int seats, List<? extends Party> parties)
	{
		this.seats = seats;
		this.parties = parties;
	}

	@Override
	public Int seats()
	{
		return seats;
	}

	@Override
	public List<? extends Party> parties()
	{
		return ImmutableList.copyOf(parties);
	}


	public static class Party implements MonopropInput.Party
	{
		private final String name;
		private final Rational votes;
		private final Int min, max, dir;
		private final List<Party> apparentment;

		public Party(String name, Rational votes, Int min, Int max, Int dir, List<Party> apparentment)
		{
			this.name = name;
			this.votes = votes;
			this.min = min;
			this.max = max;
			this.dir = dir;
			this.apparentment = apparentment;
		}

		@Override
		public String name()
		{
			return name;
		}

		@Override
		public Rational votes()
		{
			return votes;
		}

		@Override
		public Int min()
		{
			return min;
		}

		@Override
		public Int max()
		{
			return max;
		}

		@Override
		public Int dir()
		{
			return dir;
		}

		@Override
		public List<? extends MonopropInput.Party> apparentment()
		{
			return null;
		}
	}
}
