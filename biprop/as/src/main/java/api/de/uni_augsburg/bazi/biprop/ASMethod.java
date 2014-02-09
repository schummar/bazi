package de.uni_augsburg.bazi.biprop;

import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.monoprop.DivisorMethod;

import java.util.List;

public class ASMethod extends BipropMethod
{
	private final DivisorMethod divisorMethod;
	private final DivisorUpdateFunction divisorUpdateFunction;

	public ASMethod(DivisorMethod divisorMethod, DivisorUpdateFunction divisorUpdateFunction)
	{
		this.divisorMethod = divisorMethod;
		this.divisorUpdateFunction = divisorUpdateFunction;
	}

	@Override
	protected BipropResult calculate(Matrix<BipropOutput.Party> matrix, List<Int> rowSeats, List<Int> colSeats)
	{
		return ASAlgorithm.calculate(matrix, rowSeats, colSeats, divisorUpdateFunction, divisorMethod);
	}
}
