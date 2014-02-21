package de.uni_augsburg.bazi.monoprop;

import de.uni_augsburg.bazi.common.algorithm.VectorPlugin;

public class DivisorPlugin implements VectorPlugin
{
	@Override public DivisorAlgorithm algorithmForName(String name)
	{
		return null;
	}
	@Override public boolean isAlgorithm(String name)
	{
		return false;
	}
	@Override public Class<? extends DivisorAlgorithm> getAlgorithmClass()
	{
		return null;
	}

	public static class DivisorAlgorithm implements VectorAlgorithm
	{
		@Override public Class<VectorInput> getInputInterface()
		{
			return null;
		}
		@Override public VectorOutput apply(VectorInput in)
		{
			return null;
		}
	}
}
