package de.uni_augsburg.bazi.list;

import de.uni_augsburg.bazi.common.algorithm.Algorithm;
import de.uni_augsburg.bazi.common.algorithm.Options;
import de.uni_augsburg.bazi.math.BMath;

import static de.uni_augsburg.bazi.list.ListData.Party;

class ListAlgorithmImpl
{
	public static void calculate(ListData data, Algorithm Super, Algorithm sub, Options options)
	{

		data.parties().forEach(ListAlgorithmImpl::sumSubParties);

		Super.applyUnfiltered(data, options);

		data.parties().parallelStream().forEach(
			p -> {
				if (p.parties() == null || p.parties().isEmpty()) return;

				if (sub != null) sub.apply(p, options);
				else Super.apply(p, options);
			}
		);
	}

	private static void sumSubParties(Party party)
	{
		if (party.parties() == null || party.parties().size() == 0) return;

		if (!party.votes().equals(0))
		{
			Party copy = party.copy().cast(Party.class);
			copy.parties().clear();
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
