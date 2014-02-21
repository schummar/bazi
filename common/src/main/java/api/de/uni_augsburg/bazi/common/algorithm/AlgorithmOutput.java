package de.uni_augsburg.bazi.common.algorithm;

import de.uni_augsburg.bazi.common.Data;

import java.util.function.Supplier;

/**
* Created by Marco on 21.02.14.
*/
public interface AlgorithmOutput extends Data
{
	public Supplier<String> plain();
	public void supplier(Supplier<String> supplier);
}
