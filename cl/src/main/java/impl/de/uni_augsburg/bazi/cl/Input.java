package de.uni_augsburg.bazi.cl;

import de.uni_augsburg.bazi.biprop.BipropInput;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.monoprop.MonopropInput;

import java.util.List;
import java.util.stream.Collectors;

class Input implements MonopropInput, BipropInput
{
	public static List<Input> create(BaziFile baziFile)
	{
		return baziFile.seats.stream()
			.flatMap(interval -> interval.values().stream())
			.map(seats -> new Input(baziFile, seats))
			.collect(Collectors.toList());
	}

	private final BaziFile baziFile;
	private final Int seats;

	Input(BaziFile baziFile, Int seats)
	{
		this.baziFile = baziFile;
		this.seats = seats;
	}

	@Override public Int seats() { return seats; }
	@Override public List<? extends Party> parties() { return baziFile.parties; }
	@Override public List<? extends District> districts() { return baziFile.districts; }
}
