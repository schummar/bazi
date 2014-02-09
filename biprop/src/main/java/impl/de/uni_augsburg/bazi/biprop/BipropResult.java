package de.uni_augsburg.bazi.biprop;

import de.uni_augsburg.bazi.math.Real;

import java.util.List;

class BipropResult
{
	List<Real> rowDivisors;
	List<Real> colDivisors;

	BipropResult(List<Real> rowDivisors, List<Real> colDivisors)
	{
		this.rowDivisors = rowDivisors;
		this.colDivisors = colDivisors;
	}
}
