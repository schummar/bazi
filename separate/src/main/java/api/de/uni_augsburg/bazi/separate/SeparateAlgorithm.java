package de.uni_augsburg.bazi.separate;

import de.uni_augsburg.bazi.common.algorithm.MatrixAlgorithm;
import de.uni_augsburg.bazi.common.algorithm.MatrixOutput;
import de.uni_augsburg.bazi.common.algorithm.VectorAlgorithm;
import de.uni_augsburg.bazi.common.data.Data;

import java.util.Collections;
import java.util.List;

/**
 * Created by Marco on 10.03.14.
 */
public class SeparateAlgorithm extends MatrixAlgorithm<MatrixOutput>
{
	private final VectorAlgorithm<?> vector;
	public SeparateAlgorithm(VectorAlgorithm<?> vector)
	{
		this.vector = vector;
	}


	@Override public List<Object> getInputAttributes()
	{
		return Collections.emptyList();
	}


	@Override public MatrixOutput applyUnfiltered(Data in)
	{
		MatrixOutput out = in.copy(MatrixOutput.class);

		out.districts().parallelStream().forEach(d -> d.merge(vector.apply(d)));

		out.plain(new SeparatePlain(out, vector.name()));
		return out;
	}
}
