package de.uni_augsburg.bazi.quota;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

import de.uni_augsburg.bazi.common.Pair;
import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Rational;
import de.uni_augsburg.bazi.monoprop.MonopropMethod;
import de.uni_augsburg.bazi.monoprop.Uniqueness;

class QuotaMethodImpl
{
	public static QuotaMethod.Output calculate(QuotaFunction quotaFunction, ResidualHandler residualHandler, MonopropMethod.Input input)
	{
		Rational voteSum = BMath.ZERO;
		for (MonopropMethod.Input.Party party : input.parties())
			voteSum = voteSum.add(party.votes());

		Rational quota = quotaFunction.getQuota(voteSum, input.seats());

		List<Rational> quotients = new ArrayList<>();
		List<Pair<Int, Rational>> seatsAndRests = new ArrayList<>();
		for (MonopropMethod.Input.Party party : input.parties())
		{
			Rational quotient = party.votes().div(quota);
			quotients.add(quotient);
			seatsAndRests.add(Pair.of(quotient.floor(), quotient.frac()));
		}

		Int seatSum = BMath.ZERO;
		for (Pair<Int, Rational> pair : seatsAndRests)
			seatSum = seatSum.add(pair.getFirst());

		int residual = input.seats().sub(seatSum).intValue();

		ImmutableList<Pair<Integer, Uniqueness>> incrementAndUniqueness = residualHandler.getIncrementsAndUniqueness(ImmutableList.copyOf(seatsAndRests), residual);

		List<Output.Party> outputParties = new ArrayList<>();
		for (int i = 0; i < input.parties().size(); i++)
		{
			MonopropMethod.Input.Party party = input.parties().get(i);
			Rational quotient = quotients.get(i);
			Int seats = seatsAndRests.get(i).getFirst().add(incrementAndUniqueness.get(i).getFirst());
			Uniqueness uniqueness = incrementAndUniqueness.get(i).getSecond();
			outputParties.add(new Output.Party(party.name(), party.votes(), party.min(), party.max(), party.dir(), quotient, seats, uniqueness));
		}

		return new Output(input.seats(), ImmutableList.copyOf(outputParties), quota);
	}
}
