package de.uni_augsburg.bazi.common;

public interface PostProcessorPlugin<I, O>
{
	public <A extends AlgorithmPlugin.Algorithm<? super I, ? super O>> boolean isApplicableTo(Class<A> c);
	public PostProcessor<I, O> getPreProcessor();


	public interface PostProcessor<I, O>
	{
		public Class<I> getInputInterface();
		public Class<O> getOutputInterface();
		public void apply(I in, O out);
	}
}

