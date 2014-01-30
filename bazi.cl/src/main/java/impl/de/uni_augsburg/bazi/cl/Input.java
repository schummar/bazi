package de.uni_augsburg.bazi.cl;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Rational;
import de.uni_augsburg.bazi.monoprop.MonopropMethod;

class Input implements MonopropMethod.Input
{
	public Int seats;
	public List<? extends Party> parties;

	public Input()
	{
		this.seats = BMath.ZERO;
		this.parties = new ArrayList<>();
	}
	public Input(Int seats, List<? extends Party> parties)
	{
		this.seats = seats;
		this.parties = parties;
	}
	@Override public Int seats()
	{
		return seats;
	}
	@Override public ImmutableList<? extends Party> parties()
	{
		return ImmutableList.copyOf(parties);
	}


	public static class Party implements MonopropMethod.Input.Party
	{
		public String name;
		public Rational votes;
		public Int min, max, dir;

		public Party()
		{
			this.name = "";
			this.votes = BMath.ZERO;
			this.min = BMath.ZERO;
			this.max = BMath.ZERO;
			this.dir = BMath.ZERO;
		}
		public Party(String name, Rational votes, Int min, Int max, Int dir)
		{
			this.name = name;
			this.votes = votes;
			this.min = min;
			this.max = max;
			this.dir = dir;
		}
		@Override public String name()
		{
			return name;
		}
		@Override public Rational votes()
		{
			return votes;
		}
		@Override public Int min()
		{
			return min;
		}
		@Override public Int max()
		{
			return max;
		}
		@Override public Int dir()
		{
			return dir;
		}
	}
}
