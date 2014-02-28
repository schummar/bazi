package de.uni_augsburg.bazi.common.algorithm;

import de.uni_augsburg.bazi.common.util.MList;

/**
 * Created by Marco on 28.02.14.
 */
public interface MatrixOutput extends MatrixInput
{
	@Override MList<? extends VectorOutput> districts();
}
