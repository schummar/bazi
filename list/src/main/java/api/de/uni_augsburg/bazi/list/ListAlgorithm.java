package de.uni_augsburg.bazi.list;

import de.schummar.castable.Data;
import de.uni_augsburg.bazi.common.StringTable;
import de.uni_augsburg.bazi.common.algorithm.Algorithm;
import de.uni_augsburg.bazi.common.algorithm.Options;
import de.uni_augsburg.bazi.common.algorithm.VectorData;
import de.uni_augsburg.bazi.common.plain.PlainOptions;

import java.util.List;
import java.util.function.BiFunction;

/** The algorithm for combined lists. */
public class ListAlgorithm implements Algorithm<ListData>
{
	/** The algorithm used for the main apportionment. */
	public Algorithm<? extends VectorData> Super;

	/** The algorithm used for the sub apportionments. */
	public Algorithm<? extends VectorData> sub;

	/**
	 * @param Super the algorithm used for the main apportionment.
	 * @param sub the algorithm used for the main apportionment.
	 */
	public ListAlgorithm(Algorithm<? extends VectorData> Super, Algorithm<? extends VectorData> sub)
	{
		this.Super = Super.unwrap();
		this.sub = sub != null ? sub : Super;
	}

	@Override public String name() { return ""; }
	@Override public Class<ListData> dataType() { return ListData.class; }

	@Override public BiFunction<Data, PlainOptions, List<StringTable>> plainFormatter()
	{
		return (data, options) -> new ListPlain(data.cast(ListData.class), options, Super, sub).get();
	}

	@Override public void apply(Data data, Options options)
	{
		ListAlgorithmImpl.calculate(data.cast(ListData.class), Super, sub, options);
	}
}
