package de.uni_augsburg.bazi.common;

public interface AlgorithmPlugin<I extends Data, O extends Data> extends PluginManager.Plugin
{
	public Algorithm<I, O> getConstantAlgorithm(String name);
	public boolean isAlgorithm(String name);
	public Class<? extends Algorithm<I, O>> getAlgorithmClass();


	public interface Algorithm<I, O>
	{
		public Class<? extends I> getInputInterface();
		public O apply(I in);
	}
}
