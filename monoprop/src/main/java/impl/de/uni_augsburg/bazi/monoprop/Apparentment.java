package de.uni_augsburg.bazi.monoprop;

import java.util.Arrays;

class Apparentment
{
	public static <Output extends MonopropOutput> Output apply(Output output, MonopropMethod<?>... methods)
	{
		MonopropMethod<?>[] subMethods = Arrays.copyOfRange(methods, 1, methods.length);

		for (MonopropOutput.Party party : output.parties)
			if (party.parties.size() > 0)
				party.apparentment = methods[0].calculateDeep(party, subMethods);

		return output;
	}
}
