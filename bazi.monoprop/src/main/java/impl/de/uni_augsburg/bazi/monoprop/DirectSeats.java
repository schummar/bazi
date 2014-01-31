package de.uni_augsburg.bazi.monoprop;

class DirectSeats
{
	public static <Output extends MonopropOutput> Output calculate(Output output)
	{
		for (Output.Party party : output.parties)
			party.seats = party.seats.max(party.dir);
		return output;
	}
}
