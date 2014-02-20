package de.uni_ausgburg.bazi.list;

import de.uni_augsburg.bazi.common.MList;
import de.uni_augsburg.bazi.common.VectorPlugin;

public class ListPlugin implements VectorPlugin
{
	@Override public ListAlgo algorithmForName(String name)
	{
		return null;
	}
	@Override public boolean isAlgorithm(String name)
	{
		return name.equals("list");
	}
	@Override public Class<ListAlgo> getAlgorithmClass()
	{
		return ListAlgo.class;
	}

	public class ListAlgo implements VectorAlgorithm
	{
		public VectorAlgorithm main;
		public VectorAlgorithm sub;

		public ListAlgo() { }

		public ListAlgo(VectorAlgorithm main, VectorAlgorithm sub)
		{
			this.main = main;
			this.sub = sub;
		}


		@Override public Class<In> getInputInterface()
		{
			return In.class;
		}

		@Override public Out apply(VectorInput in)
		{
			VectorOutput mainOut = main.apply(in);

			MList<VectorOutput> subOuts = new MList<>();
			in.cast(In.class).parties().parallelStream().forEach(
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
