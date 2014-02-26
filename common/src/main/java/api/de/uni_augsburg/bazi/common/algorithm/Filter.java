package de.uni_augsburg.bazi.common.algorithm;

import de.uni_augsburg.bazi.common.Data;

import java.util.List;

/**
 * Created by Marco on 21.02.14.
 */
public interface Filter
{
	boolean runBefore(Algorithm algorithm);
	boolean runBefore(Filter filter);
	boolean runAfter(Algorithm algorithm);
	boolean runAfter(Filter filter);
	List<Object> getInputAttributes();
	void apply(Data in);
}
