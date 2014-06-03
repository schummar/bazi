package de.uni_augsburg.bazi.separate;

import de.schummar.castable.Data;
import de.uni_augsburg.bazi.common.algorithm.Algorithm;
import de.uni_augsburg.bazi.common.algorithm.MatrixData;
import de.uni_augsburg.bazi.common.algorithm.Options;

import java.util.Collections;
import java.util.List;

/** The algorithm for separate district evaluations. */
public class SeparateAlgorithm implements Algorithm
{
	private final Algorithm method;

	/** @param method the method used for the district apportionments. */
	public SeparateAlgorithm(Algorithm method)
	{
		this.method = method;
	}

	@Override public String name()
	{
		return "separate";
	}

	@Override public List<Object> getInputAttributes()
	{
		return Collections.emptyList();
	}


	@Override public void applyUnfiltered(Data data, Options options)
	{
		MatrixData matrixData = data.cast(MatrixData.class);
		matrixData.districts().parallelStream().forEach(d -> method.apply(d, options));
		//matrixData.plain(new SeparatePlain(out, method.name()));
	}
}
