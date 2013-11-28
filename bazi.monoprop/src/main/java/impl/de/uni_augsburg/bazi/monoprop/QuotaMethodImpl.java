package de.uni_augsburg.bazi.monoprop;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

import de.uni_augsburg.bazi.common.Pair;
import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Rational;
import de.uni_augsburg.bazi.monoprop.MonopropMethod.Input;

class QuotaMethodImpl
{
	public static MonopropMethod.Output calculate(QuotaFunction quotaFunction, ResidualHandler residualHandler, Input input)
	{
		Rational voteSum = BMath.ZERO;
		for (Input.Party party : input.getParties())
			voteSum = voteSum.add(party.getVotes());

		Rational quota = quotaFunction.getQuota(voteSum, input.getSeats());

		List<Rational> quotients = new ArrayList<>();
		List<Pair<Int, Rational>> seatsAndRests = new ArrayList<>();
		for (Input.Party party : input.getParties())
		{
			Rational quotient = party.getVotes().div(quota);
			quotients.add(quotient);
			seatsAndRests.add(Pair.of(quotient.floor(), quotient.frac()));
		}

		Int seatSum = BMath.ZERO;
		for (Pair<Int, Rational> pair : seatsAndRests)
			seatSum = seatSum.add(pair.getFirst());

		int residual = input.getSeats().sub(seatSum).intValue();

		ImmutableList<Pair<Integer, Uniqueness>> incrementAndUniqueness = residualHandler.getIncrementsAndUniqueness(ImmutableList.copyOf(seatsAndRests), residual);

		List<Output.Party> outputParties = new ArrayList<>();
		for (int i = 0; i < input.getParties().size(); i++)
		{
			Input.Party party = input.getParties().get(i);
			Rational quotient = quotients.get(i);
			Int seats = seatsAndRests.get(i).getFirst().add(incrementAndUniqueness.get(i).getFirst());
			Uniqueness uniqueness = incrementAndUniqueness.get(i).getSecond();
			outputParties.add(new Output.Party(party.getName(), party.getVotes(), party.getMin(), party.getMax(), party.getDir(), quotient, seats, uniqueness));
		}

		return new Output(input.getSeats(), ImmutableList.copyOf(outputParties));
	}
}
