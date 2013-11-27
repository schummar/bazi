package de.uni_augsburg.bazi.monoprop;

import java.util.List;

import com.google.common.collect.ImmutableList;

import de.uni_augsburg.bazi.math.Int;

public class DummyInput implements MonopropMethod.Input
{
	public Int seats;
	public List<DummyParty> parties;

	public DummyInput(Int seats, List<DummyParty> parties)
	{
		this.seats = seats;
		this.parties = parties;
	}
	@Override public Int getSeats()
	{
		return seats;
	}
	@Override public ImmutableList<? extends MonopropMethod.Input.Party> getParties()
	{
		return ImmutableList.copyOf(parties);
	}
}
