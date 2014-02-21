package de.uni_augsburg.bazi.list;

import de.uni_augsburg.bazi.common.Data;
import de.uni_augsburg.bazi.common.algorithm.VectorAlgorithm;
import de.uni_augsburg.bazi.common.algorithm.VectorInput;
import de.uni_augsburg.bazi.common.algorithm.VectorOutput;
import de.uni_augsburg.bazi.common.util.MList;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Marco on 21.02.14.
 */
public class ListAlgorithm implements VectorAlgorithm<ListInput, ListOutput>
{
	public VectorAlgorithm<?, ?> main;
	public VectorAlgorithm<?, ?> sub;

	public ListAlgorithm() { }

	public ListAlgorithm(VectorAlgorithm<?, ?> main, VectorAlgorithm<?, ?> sub)
	{
		this.main = main;
		this.sub = sub;
	}

	@Override public Class<ListInput> getInputInterface() { return ListInput.class; }
	@Override public List<Class<? extends Data>> getAllInputInterfaces()
	{
		List<Class<? extends Data>> interfaces = Arrays.asList(getInputInterface());
		interfaces.addAll(main.getAllInputInterfaces());
		interfaces.addAll(sub.getAllInputInterfaces());
		return interfaces;
	}

	@Override public ListOutput apply(ListInput in)
	{
		VectorOutput mainOut = main.applyCast(in);

		MList<VectorOutput> subOuts = new MList<>();
		in.cast(ListInput.class).parties().parallelStream().forEach(
			p -> {
				if (p.parties() == null || p.parties().isEmpty()) return;

				PartyInput subIn = mainOut.parties().find(p.name()::equals).cast(PartyInput.class);
				subIn.parties(p.parties());
				VectorOutput subOut = sub == null
					? main.applyCast(subIn)
					: sub.applyCast(subIn);

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
