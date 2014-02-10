package de.uni_augsburg.bazi.biprop;

import com.google.common.collect.Table;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Real;
import de.uni_augsburg.bazi.monoprop.DivisorMethod;

import java.util.Map;

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
	protected Map<Object, Real> calculate(Table<BipropInput.District, String, BipropOutput.Party> table, Map<Object, Int> seats)
	{
		return ASAlgorithm.calculate(table, seats, divisorUpdateFunction, divisorMethod);
	}

	@Override
	protected DivisorMethod divisorMethod()
	{
		return divisorMethod;
	}
}
