package de.uni_augsburg.bazi.common;

import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Real;

public interface VectorPlugin<P extends Data, I extends VectorPlugin.Input, O extends VectorPlugin.Output> extends AlgorithmPlugin<P, I, O>
{
	public interface Input extends Data
	{
		public String name();
		public Int seats();
		public MList<? extends Party> parties();

		public interface Party extends Data
		{
			public String name();
			public Real votes();
			public Int min();
			public Int max();
		}
	}

	public interface Output extends Input, AlgorithmPlugin.Output
	{
		public MList<? extends Party> parties();

		public void name(String name);
		public void seats(Int seats);
		public void parties(MList<? extends Party> parties);

		public interface Party extends Input.Party
		{
			public Int seats();

			public void name(String name);
			public void votes(Real votes);
			public void min(Int min);
			public void max(Int max);
			public void seats(Int seats);
		}
	}
}


