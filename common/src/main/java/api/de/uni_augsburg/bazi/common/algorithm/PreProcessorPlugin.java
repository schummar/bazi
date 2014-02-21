package de.uni_augsburg.bazi.common.algorithm;

public interface PreProcessorPlugin<I>
{
	public <O, A extends Algorithm<? super I, O>> boolean isApplicableTo(Class<A> c);
	public PreProcessor<I> getPreProcessor();
}

