package de.uni_augsburg.bazi.bmm_pow;

import de.schummar.castable.Data;
import de.uni_augsburg.bazi.common.StringTable;
import de.uni_augsburg.bazi.common.algorithm.Options;
import de.uni_augsburg.bazi.common.algorithm.VectorAlgorithm;
import de.uni_augsburg.bazi.common.plain.PlainOptions;
import de.uni_augsburg.bazi.divisor.DivisorAlgorithm;
import de.uni_augsburg.bazi.math.Int;

import java.util.List;
import java.util.function.BiFunction;

/** The base+min..max(pow) algorithm. */
public class BMMPowAlgorithm implements VectorAlgorithm
{
	/** Constraints for the seats of each party. */
	public final Int base, min, max;

	/** The algorithm to calculate the actual apportionment with. */
	public final DivisorAlgorithm method;

	/**
	 * Consreuctor with initielizers.
	 * @param base the base seats for each party.
	 * @param min the min seats for each party.
	 * @param max the max seats for each party.
	 * @param method the algorithm to calculate the actual apportionment with.
	 */
	public BMMPowAlgorithm(Int base, Int min, Int max, DivisorAlgorithm method)
	{
		this.base = base;
		this.min = min;
		this.max = max;
		this.method = method;
	}

	@Override public String name()
	{
		return "bmmpow";
	}

	@Override public BiFunction<Data, PlainOptions, List<StringTable>> plainFormatter()
	{
		return (data, options) -> new BMMPowPlain(method, data.cast(BMMPowData.class), options).get();
	}

	@Override public void applyUnfiltered(Data in, Options options)
	{
		BMMPowAlgorithmImpl.calculate(in.cast(BMMPowData.class), method, base, min, max, options);
	}
}
