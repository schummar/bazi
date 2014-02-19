package de.uni_augsburg.bazi.monoprop;

import de.uni_augsburg.bazi.common.MList;
import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Real;

import java.util.ArrayList;
import java.util.Collection;

public class MonopropOutput implements MonopropInput
{
	Int seats = BMath.ZERO;
	MList<Party> parties = new MList<>();

	public MonopropOutput() { }

	public MonopropOutput(MonopropInput input)
	{
		this.seats = input.seats();
		for (MonopropInput.Party party : input.parties())
			this.parties.add(new Party(party));
	}

	@Override
	public Int seats()
	{
		return seats;
	}

	@Override
	public MList<? extends Party> parties()
	{
		return parties;
	}

	public static class Party implements MonopropInput.Party, MonopropInput
	{
		String name = "";
		Real votes = BMath.ZERO;
		Int min = BMath.ZERO, max = BMath.INF, dir = BMath.ZERO, seats = BMath.ZERO;
		Collection<? extends Party> parties = new ArrayList<>();
		Uniqueness uniqueness = Uniqueness.UNIQUE;
		MonopropOutput apparentment;


		public Party() { }

		public Party(MonopropInput.Party party)
		{
			this.name = party.name();
			this.votes = party.votes();
			this.min = party.min();
			this.max = party.max();
			this.dir = party.dir();
			this.parties = party.parties();
		}

		@Override
		public String name() { return name; }
		@Override
		public Real votes() { return votes; }
		@Override
		public Int min() { return min; }
		@Override
		public Int max() { return max; }
		@Override
		public Int dir() { return dir; }
		@Override
		public Collection<? extends MonopropInput.Party> parties() { return parties; }
		@Override
		public Int seats() {return seats;}
		public Uniqueness uniqueness() {return uniqueness;}
		public MonopropOutput apparentment() { return apparentment; }
	}
}
