package de.uni_augsburg.bazi.list;

import de.uni_augsburg.bazi.common.algorithm.VectorInput;

import java.util.List;

/**
* Created by Marco on 21.02.14.
*/
public interface ListInput extends VectorInput
{
	List<? extends Party> parties();


	public interface Party extends VectorInput.Party
	{
		public List<ListInput.Party> parties();
	}
}
