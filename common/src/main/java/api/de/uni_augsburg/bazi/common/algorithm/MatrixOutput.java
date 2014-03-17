package de.uni_augsburg.bazi.common.algorithm;

import java.util.List;

public interface MatrixOutput extends MatrixInput
{
	@Override List<? extends VectorOutput> districts();
	void districts(List<? extends VectorOutput> districts);
}
