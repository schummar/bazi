package de.uni_augsburg.bazi.list;

import de.uni_augsburg.bazi.common.algorithm.VectorInput;

import java.util.List;

public interface ListInput extends VectorInput
{
	List<? extends Party> parties();


	public interface Party extends VectorInput.Party
	{
		public List<ListInput.Party> parties();
	}
}
