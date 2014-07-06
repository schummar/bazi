package de.uni_augsburg.bazi.as;

import com.google.common.collect.Table;
import de.uni_augsburg.bazi.biprop.BipropAlgorithm;
import de.uni_augsburg.bazi.biprop.BipropData;
import de.uni_augsburg.bazi.common.algorithm.Algorithm;
import de.uni_augsburg.bazi.common.algorithm.Options;
import de.uni_augsburg.bazi.common.algorithm.VectorData;
import de.uni_augsburg.bazi.divisor.DivisorAlgorithm;
import de.uni_augsburg.bazi.divisor.DivisorData;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Real;

import java.util.Map;

import static de.uni_augsburg.bazi.common.algorithm.VectorData.Party;

/** The alternating scaling algorithms is a biproportional algorithm. */
public class ASAlgorithm extends BipropAlgorithm<BipropData>
{
	private final Algorithm<? extends DivisorData> Super, sub;
	private final DivisorUpdateFunction divisorUpdateFunction;

	/**
	 * Constructor with initializers.
	 * @param Super the divisor algrithm to use for the super apportionment.
	 * @param sub the divisor algrithm to use for the sub apportionment.
	 * @param divisorUpdateFunction the way the divisors are updates between iterations.
	 */
	public ASAlgorithm(DivisorAlgorithm Super, Algorithm<? extends DivisorData> sub, DivisorUpdateFunction divisorUpdateFunction)
	{
		this.Super = Super;
		this.sub = sub;
		this.divisorUpdateFunction = divisorUpdateFunction;
	}

	@Override public String name()	{		return "alternating scaling";	}
	@Override public Class<BipropData> dataType()	{		return BipropData.class;	}

	@Override protected Map<Object, Real> calculate(Table<DivisorData, String, Party> table, Map<Object, Int> seats, Options options)
	{
		return ASAlgorithmImpl.calculate(table, seats, divisorUpdateFunction, Super, options);
	}

	@Override protected Algorithm<? extends DivisorData> Super()
	{
		return Super;
	}
	@Override protected Algorithm<? extends DivisorData> sub()
	{
		return Super;
	}
}
