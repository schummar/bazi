package de.uni_augsburg.bazi.quota;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Rational;
import de.uni_augsburg.bazi.monoprop.MonopropMethod;
import de.uni_augsburg.bazi.monoprop.Uniqueness;


class Output implements QuotaMethod.Output
{
	public Int seats;
	public List<? extends Party> parties;
	public Rational quota;

	public Output()
	{
		this.seats = BMath.ZERO;
		this.parties = new ArrayList<>();
		this.quota = BMath.ZERO;
	}
	public Output(Int seats, List<? extends Party> parties, Rational quota)
	{
		this.seats = seats;
		this.parties = parties;
		this.quota = quota;
	}
	@Override public Int seats()
	{
		return seats;
	}
	@Override public ImmutableList<? extends Party> parties()
	{
		return ImmutableList.copyOf(parties);
	}
	@Override public Rational getQuota()
	{
		return quota;
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
			this.max = BMath.INF;
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
		@Override public Rational quotient()
		{
			return quotient;
		}
		@Override public Int seats()
		{
			return seats;
		}
		@Override public Uniqueness uniqueness()
		{
			return uniqueness;
		}
	}
}