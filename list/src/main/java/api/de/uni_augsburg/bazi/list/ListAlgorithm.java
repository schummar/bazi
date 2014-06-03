package de.uni_augsburg.bazi.list;

import de.schummar.castable.Data;
import de.uni_augsburg.bazi.common.algorithm.Algorithm;
import de.uni_augsburg.bazi.common.algorithm.Options;

import java.util.Collections;
import java.util.List;

/** The algorithm for combined lists. */
public class ListAlgorithm implements Algorithm
{
	/** The algorithm used for the main apportionment. */
	public Algorithm Super;

	/** The algorithm used for the sub apportionments. */
	public Algorithm sub;

	/**
	 * @param Super the algorithm used for the main apportionment.
	 * @param sub the algorithm used for the main apportionment.
	 */
	public ListAlgorithm(Algorithm Super, Algorithm sub)
	{
		this.Super = Super;
		this.sub = sub;
	}

	@Override public String name() { return ""; }
	@Override public List<Object> getInputAttributes() { return Collections.emptyList(); }
	@Override public void applyUnfiltered(Data data, Options options)
	{
		ListAlgorithmImpl.calculate(data.cast(ListData.class), Super, sub, options);
	}
}
