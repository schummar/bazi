package de.uni_augsburg.bazi.monoprop;

import de.uni_augsburg.bazi.common.algorithm.Algorithm;
import de.uni_augsburg.bazi.common.algorithm.VectorPlugin;

public class DivisorAlgorithmPlugin implements VectorPlugin
{
	@Override public Algorithm<VectorInput, VectorOutput> algorithmForName(String name)
	{
		return null;
	}
	@Override public boolean isAlgorithm(String name)
	{
		return false;
	}
	@Override public Class<? extends Algorithm<VectorInput, VectorOutput>> getAlgorithmClass()
	{
		return null;
	}
}
