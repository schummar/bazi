package de.uni_augsburg.bazi.separate;

import de.schummar.castable.Data;
import de.uni_augsburg.bazi.common.StringTable;
import de.uni_augsburg.bazi.common.algorithm.Algorithm;
import de.uni_augsburg.bazi.common.algorithm.MatrixAlgorithm;
import de.uni_augsburg.bazi.common.algorithm.MatrixData;
import de.uni_augsburg.bazi.common.algorithm.Options;
import de.uni_augsburg.bazi.common.plain.PlainOptions;

import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;

/** The algorithm for separate district evaluations. */
public class SeparateAlgorithm implements MatrixAlgorithm
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

	@Override public BiFunction<Data, PlainOptions, List<StringTable>> plainFormatter()
	{
		return (data, options) -> new SeparatePlain(data.cast(MatrixData.class), options, method.name()).get();
	}

	@Override public void applyUnfiltered(Data data, Options options)
	{
		MatrixData matrixData = data.cast(MatrixData.class);
		matrixData.districts().parallelStream().forEach(d -> method.apply(d, options));
		//matrixData.plain(new SeparatePlain(out, method.name()));
	}
}
