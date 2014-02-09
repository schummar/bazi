package de.uni_augsburg.bazi.monoprop;

import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Real;

public interface ResidualHandler
{
	public ShiftQueue.ShiftFunction getShiftFunction(Real quota);


	public static ResidualHandler

		GREATEST_REMINDERS =
		quota -> (party, seats) -> {
			Real quotient = party.votes().div(quota);
			Int initialSeats = quotient.floor();
			if (seats.compareTo(initialSeats.add(1)) > 0) return BMath.ZERO;
			if (seats.compareTo(initialSeats) <= 0) return BMath.INF;
			return quotient.frac();
		},

	GREATEST_REMAINDERS_MIN =
		quota -> (party, seats) -> {
			Real quotient = party.votes().div(quota);
			Int initialSeats = quotient.floor();
			if (initialSeats.equals(BMath.ZERO) || seats.compareTo(initialSeats.add(1)) > 0) return BMath.ZERO;
			if (seats.compareTo(initialSeats) <= 0) return BMath.INF;
			return quotient.frac();
		},

	WINNER_TAKES_ALL = quota -> (party, seats) -> {
		Real quotient = party.votes().div(quota);
		Int initialSeats = quotient.floor();
		if (seats.compareTo(initialSeats) <= 0) return BMath.INF;
		return quotient;
	};
}
