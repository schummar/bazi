package de.uni_augsburg.bazi.common.algorithm;

import de.uni_augsburg.bazi.common.data.Data;

import java.util.List;

/**
 * Created by Marco on 28.02.14.
 */
public interface MatrixInput extends Data
{
	List<? extends VectorInput> districts();
}
