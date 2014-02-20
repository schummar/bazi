package de.uni_augsburg.bazi.vector;

import de.uni_augsburg.bazi.common.MList;
import de.uni_augsburg.bazi.math.Int;

public interface VectorOutput extends VectorInput
{
	public MList<? extends Party> parties();
	public interface Party extends VectorInput.Party
	{
		public Int seats();
	}
}
