package de.uni_augsburg.bazi.common.algorithm;

import de.uni_augsburg.bazi.common.data.Data;

import java.util.List;

public interface MatrixInput extends Data
{
	String name();
	List<? extends VectorInput> districts();
}
