package de.uni_augsburg.bazi.biprop;

import com.google.common.collect.Table;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Real;
import de.uni_augsburg.bazi.monoprop.DivisorAlgorithm;

import java.util.Map;

public class ASMethod extends BipropMethod
{
	private final DivisorAlgorithm divisorAlgorithm;
	private final DivisorUpdateFunction divisorUpdateFunction;

	public ASMethod(DivisorAlgorithm divisorAlgorithm, DivisorUpdateFunction divisorUpdateFunction)
	{
		this.divisorAlgorithm = divisorAlgorithm;
		this.divisorUpdateFunction = divisorUpdateFunction;
	}

	@Override
	protected Map<Object, Real> calculate(Table<BipropInput.District, String, BipropOutput.Party> table, Map<Object, Int> seats)
	{
		return ASAlgorithm.calculate(table, seats, divisorUpdateFunction, divisorAlgorithm);
	}

	@Override
	protected DivisorAlgorithm divisorMethod()
	{
		return divisorAlgorithm;
	}
}
