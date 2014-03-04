package de.uni_augsburg.bazi.list;

import de.uni_augsburg.bazi.common.algorithm.VectorAlgorithm;
import de.uni_augsburg.bazi.common.algorithm.VectorOutput;
import de.uni_augsburg.bazi.math.BMath;

import static de.uni_augsburg.bazi.list.ListOutput.Party;

/**
 * Created by Marco on 27.02.14.
 */
class ListAlgorithmImpl
{
	private static void check(ListInput in)
	{

	}

	public static ListOutput calculate(ListInput in, VectorAlgorithm main, VectorAlgorithm sub)
	{
		check(in);
		ListOutput out = in.copy(ListOutput.class);

		out.parties().forEach(ListAlgorithmImpl::sumSubParties);

		out.merge(main.apply(in));

		out.parties().parallelStream().forEach(
			p -> {
				if (p.parties() == null || p.parties().isEmpty()) return;

				VectorOutput subOut = sub == null
					? main.apply(p)
					: sub.apply(p);
				p.merge(subOut);
			}
		);

		return out;
	}

	private static void sumSubParties(Party party)
	{
		if (party.parties() == null || party.parties().size() == 0) return;

		if (!party.votes().equals(0))
		{
			Party copy = party.copy().cast(Party.class);
			party.parties().add(0, copy);
			party.votes(BMath.ZERO);
			party.min(BMath.ZERO);
			party.max(BMath.ZERO);
		}

		party.parties().forEach(
			p -> {
				sumSubParties(p);
				party.votes(party.votes().add(p.votes()));
				party.min(party.min().add(p.min()));
				party.max(party.max().add(p.max()));
			}
		);
	}
}
