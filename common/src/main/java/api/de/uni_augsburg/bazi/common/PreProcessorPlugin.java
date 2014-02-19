package de.uni_augsburg.bazi.common;

public interface PreProcessorPlugin<I>
{
	public <O, A extends AlgorithmPlugin.Algorithm<? super I, O>> boolean isApplicableTo(Class<A> c);
	public PreProcessor<I> getPreProcessor();


	public interface PreProcessor<I>
	{
		public Class<I> getInputInterface();
		public void apply(I data);
	}
}

