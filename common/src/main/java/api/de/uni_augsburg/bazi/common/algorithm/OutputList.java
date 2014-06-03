package de.uni_augsburg.bazi.common.algorithm;

import de.schummar.castable.Data;

import java.util.List;

/** A calculation result that is a list of results. */
public interface OutputList
{
	/**
	 * The list of results.
	 * @return the list of results.
	 */
	List<? extends Data> results();
}
