package de.uni_augsburg.bazi.cl;

import com.google.common.collect.ImmutableList;
import de.uni_augsburg.bazi.math.Int;
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
}
