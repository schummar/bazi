package de.uni_augsburg.bazi.as;

import com.google.common.collect.Table;
import de.uni_augsburg.bazi.biprop.BipropAlgorithm;
import de.uni_augsburg.bazi.common.algorithm.Options;
import de.uni_augsburg.bazi.divisor.DivisorAlgorithm;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Real;

import java.util.Map;

import static de.uni_augsburg.bazi.biprop.BipropOutput.District;
import static de.uni_augsburg.bazi.common.algorithm.VectorOutput.Party;

public class ASAlgorithm extends BipropAlgorithm
{
	private final DivisorAlgorithm Super, sub;
	private final DivisorUpdateFunction divisorUpdateFunction;

	public ASAlgorithm(DivisorAlgorithm Super, DivisorAlgorithm sub, DivisorUpdateFunction divisorUpdateFunction)
	{
		this.Super = Super;
		this.sub = sub;
		this.divisorUpdateFunction = divisorUpdateFunction;
	}

	@Override public String name()
	{
		return "alternating scaling";
	}

	@Override protected Map<Object, Real> calculate(Table<District, String, Party> table, Map<Object, Int> seats, Options options)
	{
		return ASAlgorithmImpl.calculate(table, seats, divisorUpdateFunction, Super, options);
	}

	@Override protected DivisorAlgorithm Super()
	{
		return Super;
	}
	@Override protected DivisorAlgorithm sub()
	{
		return Super;
	}
}
