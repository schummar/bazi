package de.uni_augsburg.bazi.list;

import de.uni_augsburg.bazi.common.algorithm.VectorInput;
import de.uni_augsburg.bazi.common.util.MList;

/**
* Created by Marco on 21.02.14.
*/
public interface ListInput extends VectorInput
{
	MList<? extends Party> parties();


	public interface Party extends VectorInput.Party
	{
		public MList<ListInput.Party> parties();
	}
}
