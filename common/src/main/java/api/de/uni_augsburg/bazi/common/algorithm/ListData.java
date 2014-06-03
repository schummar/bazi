package de.uni_augsburg.bazi.common.algorithm;

import de.schummar.castable.Data;
import de.schummar.castable.DataList;

/** A calculation result that is a list of results. */
public interface ListData
{
	/**
	 * The list of results.
	 * @return the list of results.
	 */
	DataList<? extends Data> results();
}
