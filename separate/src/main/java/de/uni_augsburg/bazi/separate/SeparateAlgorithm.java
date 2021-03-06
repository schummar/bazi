package de.uni_augsburg.bazi.separate;

import de.schummar.castable.Data;
import de.uni_augsburg.bazi.common.StringTable;
import de.uni_augsburg.bazi.common.algorithm.Algorithm;
import de.uni_augsburg.bazi.common.algorithm.MatrixData;
import de.uni_augsburg.bazi.common.algorithm.Options;
import de.uni_augsburg.bazi.common.algorithm.VectorData;
import de.uni_augsburg.bazi.common.plain.PlainOptions;

import java.util.List;
import java.util.function.BiFunction;

/** The algorithm for separate district evaluations. */
public class SeparateAlgorithm implements Algorithm<MatrixData>
{
	public final Algorithm<? extends VectorData> method;

	/** @param method the method used for the district apportionments. */
	public SeparateAlgorithm(Algorithm<? extends VectorData> method)
	{
		this.method = method;
	}

	@Override public String name() { return "separate"; }
	@Override public Class<MatrixData> dataType() { return MatrixData.class; }

	@Override public BiFunction<Data, PlainOptions, List<StringTable>> plainFormatter()
	{
		return (data, options) -> new SeparatePlain(data.cast(MatrixData.class), options, method).get();
	}

	@Override public void apply(Data data, Options options)
	{
		data.cast(MatrixData.class).districts().parallelStream()
			.forEach(d -> method.apply(d, options));
	}
}
