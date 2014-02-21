package de.uni_augsburg.bazi.common.algorithm;

public interface PreProcessorPlugin<I>
{
	public <A extends Algorithm<? super I, ?>> boolean isApplicableTo(Class<A> c);
	public PreProcessor<I> getPreProcessor();
}

