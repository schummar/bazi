package de.uni_augsburg.bazi.cl;

import com.google.common.collect.ImmutableList;
import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Rational;
import de.uni_augsburg.bazi.monoprop.MonopropInput;

import java.util.ArrayList;
import java.util.List;

class Input implements MonopropInput
{
	public Int seats = BMath.ZERO;
	public List<? extends Party> parties = new ArrayList<>();

	public Input() { }

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
		public String name = "";
		public Rational votes = BMath.ZERO;
		public Int min = BMath.ZERO, max = BMath.INF, dir = BMath.ZERO;
		public List<Party> apparentment = new ArrayList<>();

		public Party() { }

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
