package de.uni_augsburg.bazi.common.algorithm;

import de.uni_augsburg.bazi.common.Data;

/**
 * Created by Marco on 28.02.14.
 */
public interface MatrixAlgorithm extends Algorithm
{
	MatrixOutput apply(MatrixInput in);
	@Override default Data apply(Data in) { return apply(in.cast(MatrixInput.class)); }
}
