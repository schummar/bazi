package de.uni_augsburg.bazi.list;

import de.uni_augsburg.bazi.common.algorithm.Options;
import de.uni_augsburg.bazi.common.algorithm.VectorAlgorithm;
import de.uni_augsburg.bazi.common.data.Data;

import java.util.Collections;
import java.util.List;

/** The algorithm for combined lists. */
public class ListAlgorithm implements VectorAlgorithm<ListOutput>
{
	/** The algorithm used for the main apportionment. */
	public VectorAlgorithm<?> Super;

	/** The algorithm used for the sub apportionments. */
	public VectorAlgorithm<?> sub;

	/**
	 * @param Super the algorithm used for the main apportionment.
	 * @param sub the algorithm used for the main apportionment.
	 */
	public ListAlgorithm(VectorAlgorithm<?> Super, VectorAlgorithm<?> sub)
	{
		this.Super = Super;
		this.sub = sub;
	}

	@Override public String name() { return ""; }
	@Override public List<Object> getInputAttributes() { return Collections.emptyList(); }
	@Override public ListOutput applyUnfiltered(Data in, Options options)
	{
		return ListAlgorithmImpl.calculate(in.cast(ListInput.class), Super, sub, options);
	}
}
