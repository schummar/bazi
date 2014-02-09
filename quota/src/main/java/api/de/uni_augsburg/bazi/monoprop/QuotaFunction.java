package de.uni_augsburg.bazi.monoprop;

import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Real;

public interface QuotaFunction
{
	public Real getQuota(Real votes, Int seats);


	public static final QuotaFunction
		HARE = (votes, seats) -> votes.div(seats),
		HARE_VAR1 = (votes, seats) -> votes.div(seats).floor(),
		HARE_VAR2 = (votes, seats) -> votes.div(seats).ceil(),
		DROOP = (votes, seats) -> votes.div(seats.add(1)).floor().add(1),
		DROOP_VAR1 = (votes, seats) -> votes.div(seats.add(1)).floor().max(1),
		DROOP_VAR2 = (votes, seats) -> votes.div(seats.add(1)).ceil(),
		DROOP_VAR3 = (votes, seats) -> votes.div(seats.add(1));
}
