package de.uni_augsburg.bazi.biprop;

import com.google.common.collect.Table;
import de.uni_augsburg.bazi.divisor.DivisorAlgorithm;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Real;

import java.util.Map;

import static de.uni_augsburg.bazi.biprop.BipropOutput.District;
import static de.uni_augsburg.bazi.common.algorithm.VectorOutput.Party;

public class ASAlgorithm extends BipropAlgorithm
{
	private final DivisorAlgorithm divisorAlgorithm;
	private final DivisorUpdateFunction divisorUpdateFunction;

	public ASAlgorithm(DivisorAlgorithm divisorAlgorithm, DivisorUpdateFunction divisorUpdateFunction)
	{
		this.divisorAlgorithm = divisorAlgorithm;
		this.divisorUpdateFunction = divisorUpdateFunction;
	}


	@Override
	protected Map<Object, Real> calculate(Table<District, String, Party> table, Map<Object, Int> seats)
	{
		return ASAlgorithmImpl.calculate(table, seats, divisorUpdateFunction, divisorAlgorithm);
	}

	@Override
	protected DivisorAlgorithm divisorMethod()
	{
		return divisorAlgorithm;
	}
}
