package de.uni_ausgburg.bazi.list;

import de.uni_augsburg.bazi.common.MList;
import de.uni_augsburg.bazi.vector.VectorPlugin;

public class ListPlugin implements VectorPlugin
{
	@Override public ListAlgo getConstantAlgorithm(String name)
	{
		return new ListAlgo();
	}
	@Override public boolean isAlgorithm(String name)
	{
		return false;
	}
	@Override public Class<ListAlgo> getAlgorithmClass()
	{
		return ListAlgo.class;
	}

	public class ListAlgo implements VectorAlgorithm
	{
		public VectorAlgorithm mainAlgorithm;
		public VectorAlgorithm subAlgorithm;

		public ListAlgo() { }

		public ListAlgo(VectorAlgorithm mainAlgorithm, VectorAlgorithm subAlgorithm)
		{
			this.mainAlgorithm = mainAlgorithm;
			this.subAlgorithm = subAlgorithm;
		}


		@Override public Class<In> getInputInterface()
		{
			return In.class;
		}

		@Override public Out apply(VectorInput in)
		{
			VectorOutput mainOut = mainAlgorithm.apply(in);

			MList<VectorOutput> subOuts = new MList<>();
			in.cast(In.class).parties().parallelStream().forEach(
				p -> {
					if (p.parties() == null || p.parties().isEmpty()) return;

					PartyInput subIn = mainOut.parties().find(p.name()::equals).cast(PartyInput.class);
					subIn.parties(p.parties());
					VectorOutput subOut = subAlgorithm == null
						? mainAlgorithm.apply(subIn)
						: subAlgorithm.apply(subIn);

					synchronized (subOuts)
					{
						subOuts.add(subOut);
					}
				}
			);

			return mainOut.cast(Out.class).subApportionments(subOuts);
		}
	}

	public interface In extends VectorInput
	{
		public MList<? extends Party> parties();
		public interface Party extends VectorInput.Party
		{
			public MList<VectorInput.Party> parties();
		}
	}

	private interface PartyInput extends VectorOutput.Party, VectorInput
	{
		public PartyInput parties(MList<VectorInput.Party> parties);
	}

	public interface Out extends VectorOutput
	{
		public MList<VectorOutput> subApportionments();
		public Out subApportionments(MList<VectorOutput> subApportionments);
	}
}
