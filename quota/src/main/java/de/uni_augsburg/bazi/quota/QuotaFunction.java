package de.uni_augsburg.bazi.quota;

import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Real;

/** A quota function calculates a quota out of votes and seats. */
public interface QuotaFunction
{
	/**
	 * Calculates a quota out of votes and seats.
	 * @param votes the votes.
	 * @param seats the seats.
	 * @return the quota.
	 */
	public Real apply(Real votes, Int seats);


	public static final QuotaFunction
		HARE = (votes, seats) -> votes.div(seats),
		HARE_VAR1 = (votes, seats) -> votes.div(seats).floor(),
		HARE_VAR2 = (votes, seats) -> votes.div(seats).ceil(),
		DROOP = (votes, seats) -> votes.div(seats.add(1)).floor().add(1),
		DROOP_VAR1 = (votes, seats) -> votes.div(seats.add(1)).floor().max(1),
		DROOP_VAR2 = (votes, seats) -> votes.div(seats.add(1)).ceil(),
		DROOP_VAR3 = (votes, seats) -> votes.div(seats.add(1));
}
