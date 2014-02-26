package de.uni_augsburg.bazi.list;

import de.uni_augsburg.bazi.common.algorithm.VectorAlgorithm;
import de.uni_augsburg.bazi.common.algorithm.VectorInput;
import de.uni_augsburg.bazi.common.algorithm.VectorOutput;
import de.uni_augsburg.bazi.common.util.MList;

import java.util.Collections;
import java.util.List;

/**
 * Created by Marco on 21.02.14.
 */
public class ListAlgorithm implements VectorAlgorithm
{
	public VectorAlgorithm main;
	public VectorAlgorithm sub;

	public ListAlgorithm(VectorAlgorithm main, VectorAlgorithm sub)
	{
		this.main = main;
		this.sub = sub;
	}

	@Override public List<Object> getInputAttributes() { return Collections.emptyList(); }
	@Override public ListOutput apply(VectorInput in) { return apply(in.cast(ListInput.class)); }

	public ListOutput apply(ListInput in)
	{

		VectorOutput mainOut = main.apply(in);

		MList<VectorOutput> subOuts = new MList<>();
		in.cast(ListInput.class).parties().parallelStream().forEach(
			p -> {
				if (p.parties() == null || p.parties().isEmpty()) return;

				PartyInput subIn = mainOut.parties().find(p.name()::equals).cast(PartyInput.class);
				subIn.parties(p.parties());
				VectorOutput subOut = sub == null
					? main.apply(subIn)
					: sub.apply(subIn);

				synchronized (subOuts)
				{
					subOuts.add(subOut);
				}
			}
		);

		ListOutput out = mainOut.cast(ListOutput.class);
		out.subApportionments(subOuts);
		return out;
	}


	private interface PartyInput extends VectorOutput.Party, VectorInput
	{
		public PartyInput parties(MList<VectorInput.Party> parties);
	}
}
