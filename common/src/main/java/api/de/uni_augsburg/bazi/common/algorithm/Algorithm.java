package de.uni_augsburg.bazi.common.algorithm;

import java.util.List;

/**
* Created by Marco on 21.02.14.
*/
public interface Algorithm<I, O>
{
	public List<Class<? extends I>> getInputInterface();
	public O apply(I in);
}
