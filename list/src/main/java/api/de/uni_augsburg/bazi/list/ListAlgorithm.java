package de.uni_augsburg.bazi.list;

import de.uni_augsburg.bazi.common.algorithm.VectorAlgorithm;
import de.uni_augsburg.bazi.common.data.Data;

import java.util.Collections;
import java.util.List;

/**
 * Created by Marco on 21.02.14.
 */
public class ListAlgorithm extends VectorAlgorithm<ListOutput>
{
	public VectorAlgorithm<?> Super;
	public VectorAlgorithm<?> sub;

	public ListAlgorithm(VectorAlgorithm<?> Super, VectorAlgorithm<?> sub)
	{
		this.Super = Super;
		this.sub = sub;
	}

	@Override public String name() { return ""; }
	@Override public List<Object> getInputAttributes() { return Collections.emptyList(); }
	@Override public ListOutput applyUnfiltered(Data in)
	{
		return ListAlgorithmImpl.calculate(in.cast(ListInput.class), Super, sub);
	}
}
