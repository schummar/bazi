package de.uni_augsburg.bazi.common.algorithm;

import de.uni_augsburg.bazi.common.Data;
import de.uni_augsburg.bazi.common.PluginManager;

public interface AlgorithmPlugin<P extends Data, I extends Data, O extends AlgorithmOutput> extends PluginManager.Plugin
{
	public Class<P> getParamsInterface();
	public Algorithm<I, O> createAlgoritm(String name, P params);
}
