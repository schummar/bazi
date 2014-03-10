package de.uni_augsburg.bazi.common.algorithm;

import java.util.List;

/**
 * Created by Marco on 28.02.14.
 */
public interface MatrixOutput extends MatrixInput
{
	@Override List<? extends VectorOutput> districts();
	void districts(List<? extends VectorOutput> districts);
}
