package de.uni_augsburg.bazi.vector;

import de.uni_augsburg.bazi.common.AlgorithmPlugin;

public interface VectorPlugin extends AlgorithmPlugin<VectorInput, VectorOutput>
{
	public interface VectorAlgorithm extends Algorithm<VectorInput, VectorOutput> {}

}
