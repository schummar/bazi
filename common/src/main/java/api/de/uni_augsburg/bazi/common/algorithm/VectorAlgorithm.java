package de.uni_augsburg.bazi.common.algorithm;

import de.uni_augsburg.bazi.common.data.Data;

/**
 * Created by Marco on 21.02.14.
 */
public interface VectorAlgorithm extends Algorithm
{
	VectorOutput apply(VectorInput in);
	@Override default VectorOutput apply(Data in) { return apply(in.cast(VectorInput.class)); }
}
