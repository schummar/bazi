package de.uni_augsburg.bazi.list;

import de.schummar.castable.Data;
import de.uni_augsburg.bazi.common.StringTable;
import de.uni_augsburg.bazi.common.algorithm.Options;
import de.uni_augsburg.bazi.common.algorithm.VectorAlgorithm;
import de.uni_augsburg.bazi.common.plain.PlainOptions;

import java.util.List;
import java.util.function.BiFunction;

/** The algorithm for combined lists. */
public class ListAlgorithm implements VectorAlgorithm
{
	/** The algorithm used for the main apportionment. */
	public VectorAlgorithm Super;

	/** The algorithm used for the sub apportionments. */
	public VectorAlgorithm sub;

	/**
	 * @param Super the algorithm used for the main apportionment.
	 * @param sub the algorithm used for the main apportionment.
	 */
	public ListAlgorithm(VectorAlgorithm Super, VectorAlgorithm sub)
	{
		this.Super = Super;
		this.sub = sub;
	}

	@Override public String name() { return ""; }


	@Override public BiFunction<Data, PlainOptions, List<StringTable>> plainFormatter()
	{
		return (data, options) -> new ListPlain(data.cast(ListData.class), options, Super, sub).get();
	}

	@Override public void applyUnfiltered(Data data, Options options)
	{
		ListAlgorithmImpl.calculate(data.cast(ListData.class), Super, sub, options);
	}
}
