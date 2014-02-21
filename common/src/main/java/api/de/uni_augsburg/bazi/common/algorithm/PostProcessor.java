package de.uni_augsburg.bazi.common.algorithm;

/**
* Created by Marco on 21.02.14.
*/
public interface PostProcessor<I, O>
{
	public Class<I> getInputInterface();
	public Class<O> getOutputInterface();
	public void apply(I in, O out);
}
