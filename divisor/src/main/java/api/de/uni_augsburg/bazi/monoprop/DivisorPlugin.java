package de.uni_augsburg.bazi.monoprop;


import de.uni_augsburg.bazi.common.Data;
import de.uni_augsburg.bazi.common.algorithm.Algorithm;
import de.uni_augsburg.bazi.common.algorithm.VectorInput;
import de.uni_augsburg.bazi.common.algorithm.VectorPlugin;

public class DivisorPlugin implements VectorPlugin<DivisorPlugin.Params, VectorInput, DivisorOutput>
{
	@Override public Class<Params> getParamsInterface() { return Params.class; }

	@Override public Algorithm<VectorInput, DivisorOutput> createAlgoritm(String name, Params params)
	{
		return new DivisorAlgorithm(params.roundingFunction(), params.minPrecision());
	}


	public interface Params extends Data
	{
		public RoundingFunction roundingFunction();
		public int minPrecision();
	}
}
