package de.uni_augsburg.bazi.list;

import de.uni_augsburg.bazi.common.algorithm.VectorInput;

import java.util.List;

/** Input for the ListAlgorithm. */
public interface ListInput extends VectorInput
{
	@Override List<? extends Party> parties();


	public interface Party extends VectorInput.Party
	{
		/**
		 * The parties in this combined list.
		 * @return the parties in this combined list.
		 */
		public List<ListInput.Party> parties();
	}
}
