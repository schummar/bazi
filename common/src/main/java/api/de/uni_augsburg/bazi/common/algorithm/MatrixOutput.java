package de.uni_augsburg.bazi.common.algorithm;

import java.util.List;

/** A minimal maxtrix output interface. */
public interface MatrixOutput extends MatrixInput
{
	@Override List<? extends VectorOutput> districts();

	/**
	 * The list of one dimensional output which represent one district each.
	 * @param districts the list of one dimensional output which represent one district each.
	 */
	void districts(List<? extends VectorOutput> districts);
}
