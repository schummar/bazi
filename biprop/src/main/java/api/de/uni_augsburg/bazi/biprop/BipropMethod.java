package de.uni_augsburg.bazi.biprop;

import de.uni_augsburg.bazi.math.Int;

import java.util.List;

public abstract class BipropMethod
{
	public BipropOutput calculate(BipropInput input)
	{
		return null;
	}

	protected abstract BipropResult calculate(Matrix<BipropOutput.Party> matrix, List<Int> rowSeats, List<Int> colSeats);
}
