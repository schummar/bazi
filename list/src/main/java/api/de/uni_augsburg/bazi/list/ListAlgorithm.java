package de.uni_augsburg.bazi.list;

import de.uni_augsburg.bazi.common.algorithm.VectorAlgorithm;
import de.uni_augsburg.bazi.common.algorithm.VectorInput;

import java.util.Collections;
import java.util.List;

/**
 * Created by Marco on 21.02.14.
 */
public class ListAlgorithm implements VectorAlgorithm
{
	public VectorAlgorithm main;
	public VectorAlgorithm sub;

	public ListAlgorithm(VectorAlgorithm main, VectorAlgorithm sub)
	{
		this.main = main;
		this.sub = sub;
	}

	@Override public List<Object> getInputAttributes() { return Collections.emptyList(); }
	@Override public ListOutput apply(VectorInput in) { return apply(in.cast(ListInput.class)); }

	public ListOutput apply(ListInput in)
	{
		return ListAlgorithmImpl.calculate(in, main, sub);
	}
}
