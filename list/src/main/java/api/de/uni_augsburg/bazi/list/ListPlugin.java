package de.uni_augsburg.bazi.list;

import de.uni_augsburg.bazi.common.Data;
import de.uni_augsburg.bazi.common.algorithm.Algorithm;
import de.uni_augsburg.bazi.common.algorithm.VectorPlugin;

public class ListPlugin implements VectorPlugin<Data, ListInput, ListOutput>
{
	@Override public Class<Data> getParamsInterface()
	{
		return Data.class;
	}
	@Override public Algorithm<ListInput, ListOutput> createAlgoritm(String name, Data params)
	{
		return name.equals("list") ? new ListAlgorithm() : null;
	}
}
