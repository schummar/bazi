package de.uni_augsburg.bazi.common;

import java.util.List;
import java.util.function.Supplier;

public interface AlgorithmPlugin<P extends Data, I extends Data, O extends AlgorithmPlugin.Output> extends PluginManager.Plugin
{
	public Class<P> getParamsInterface();
	public Algorithm<I, O> createAlgoritm(String name, P params);

	public interface Algorithm<I, O>
	{
		public List<Class<? extends I>> getInputInterface();
		public O apply(I in);
	}

	public interface Output extends Data
	{
		public Supplier<String> plain();
		public void supplier(Supplier<String> supplier);
	}
}
