package de.uni_augsburg.bazi.biprop;

import com.google.common.collect.Table;
import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Real;
import de.uni_augsburg.bazi.monoprop.DivisorOutput;
import de.uni_augsburg.bazi.monoprop.MonopropInput;
import de.uni_augsburg.bazi.monoprop.Uniqueness;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class BipropOutput
{
	DivisorOutput superApportionment;
	Table<BipropInput.District, String, Party> table;
	Map<Object, Int> seats;
	Map<Object, Real> divisors;

	public BipropOutput(DivisorOutput superApportionment, Table<BipropInput.District, String, Party> table, Map<Object, Int> seats, Map<Object, Real> divisors)
	{
		this.superApportionment = superApportionment;
		this.table = table;
		this.seats = seats;
		this.divisors = divisors;
	}

	public Table<BipropInput.District, String, Party> table() { return table; }
	public Map<Object, Int> seats() { return seats; }
	public Map<Object, Real> divisors() { return divisors; }


	public static class Party implements MonopropInput.Party
	{
		String name = "";
		Real votes = BMath.ZERO;
		Int min = BMath.ZERO, max = BMath.INF, dir = BMath.ZERO, seats = BMath.ZERO;
		Uniqueness uniqueness = Uniqueness.UNIQUE;
		Collection<? extends MonopropInput.Party> parties = new ArrayList<>();

		public Party(MonopropInput.Party party)
		{
			this.name = party.name();
			this.votes = party.votes();
			this.min = party.min();
			this.max = party.max();
			this.dir = party.dir();
			this.parties = party.parties();
		}

		public Party(String name)
		{
			this.name = name;
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
		public Int seats() { return seats; }
		public Uniqueness uniqueness() { return uniqueness; }
		@Override
		public Collection<? extends MonopropInput.Party> parties() { return parties; }
	}
}
