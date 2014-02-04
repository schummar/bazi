package de.uni_augsburg.bazi.monoprop;

import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Rational;

import java.util.ArrayList;
import java.util.List;

public class MonopropOutput implements MonopropInput
{
	Int seats = BMath.ZERO;
	List<Party> parties = new ArrayList<>();

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
	public List<? extends Party> parties()
	{
		return parties;
	}

	public static class Party implements MonopropInput.Party, MonopropInput
	{
		Object id = this;
		String name = "";
		Rational votes = BMath.ZERO;
		Int min = BMath.ZERO, max = BMath.INF, dir = BMath.ZERO, seats = BMath.ZERO;
		List<? extends MonopropInput.Party> parties = new ArrayList<>();
		Uniqueness uniqueness = Uniqueness.UNIQUE;
		MonopropOutput apparentment;


		public Party() { }

		public Party(MonopropInput.Party party)
		{
			this.id = party.id();
			this.name = party.name();
			this.votes = party.votes();
			this.min = party.min();
			this.max = party.max();
			this.dir = party.dir();
			this.parties = party.parties();
		}

		@Override
		public Object id() { return id; }
		@Override
		public String name() { return name; }
		@Override
		public Rational votes() { return votes; }
		@Override
		public Int min() { return min; }
		@Override
		public Int max() { return max; }
		@Override
		public Int dir() { return dir; }
		@Override
		public List<? extends MonopropInput.Party> parties() { return parties; }
		@Override
		public Int seats() {return seats;}
		public Uniqueness uniqueness() {return uniqueness;}
		public MonopropOutput apparentment() { return apparentment; }

		@Override
		public boolean equals(Object that)
		{
			return this == that
						 || (
				that != null
				&& that instanceof MonopropInput.Party
				&& id().equals(((MonopropInput.Party) that).id())
			);
		}

		@Override
		public int hashCode()
		{
			return System.identityHashCode(id);
		}
	}
}