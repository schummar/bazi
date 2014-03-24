package de.uni_augsburg.bazi.common.algorithm;

import de.uni_augsburg.bazi.common.data.Data;

import java.util.List;

/** A minimal maxtrix input interface. */
public interface MatrixInput extends Data
{
	/**
	 * The name of the apportionment.
	 * @return the name of the apportionment.
	 */
	String name();

	/**
	 * The list of one dimensional inputs which represent one district each.
	 * @return the list of one dimensional inputs which represent one district each.
	 */
	List<? extends VectorInput> districts();
}
