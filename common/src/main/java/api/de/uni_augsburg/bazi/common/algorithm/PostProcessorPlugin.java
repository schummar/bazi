package de.uni_augsburg.bazi.common.algorithm;

public interface PostProcessorPlugin<I, O>
{
	public <A extends Algorithm<? super I, ? super O>> boolean isApplicableTo(Class<A> c);
	public PostProcessor<I, O> getPreProcessor();
}

