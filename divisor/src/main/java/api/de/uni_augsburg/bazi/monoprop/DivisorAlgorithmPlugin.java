package de.uni_augsburg.bazi.monoprop;

import de.uni_augsburg.bazi.vector.VectorInput;
import de.uni_augsburg.bazi.vector.VectorOutput;
import de.uni_augsburg.bazi.vector.VectorPlugin;

public class DivisorAlgorithmPlugin implements VectorPlugin
{
	@Override public Algorithm<VectorInput, VectorOutput> getConstantAlgorithm(String name)
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
