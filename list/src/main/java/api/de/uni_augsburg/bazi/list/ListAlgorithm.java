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
	public VectorAlgorithm<?> main;
	public VectorAlgorithm<?> sub;

	public ListAlgorithm(VectorAlgorithm<?> main, VectorAlgorithm<?> sub)
	{
		this.main = main;
		this.sub = sub;
	}

	@Override public String name() { return ""; }
	@Override public List<Object> getInputAttributes() { return Collections.emptyList(); }
	@Override public ListOutput applyUnfiltered(Data in)
	{
		return ListAlgorithmImpl.calculate(in.cast(ListInput.class), main, sub);
	}
}
