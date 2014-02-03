package de.uni_augsburg.bazi.monoprop;

import de.uni_augsburg.bazi.math.Int;

import java.util.ArrayList;
import java.util.List;

class Apparentment
{
	public static <Output extends MonopropOutput, Method extends MonopropMethod> List<Output> calculate(Output output, Method method)
	{
		List<Output> list = new ArrayList<>();
		list.add(output);

		for (Output.Party party : output.parties)
		{
			//list.addAll(method.calculateAll(new Input(party.seats, party.apparentment)));
		}

		return list;
	}

	private static class Input implements MonopropInput
	{
		Int seats;
		List<? extends Party> parties;

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
			return parties;
		}
	}
}
