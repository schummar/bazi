package de.uni_augsburg.bazi.common.algorithm;

/**
* Created by Marco on 21.02.14.
*/
public interface PreProcessor<I>
{
	public Class<I> getInputInterface();
	public void apply(I data);
}
