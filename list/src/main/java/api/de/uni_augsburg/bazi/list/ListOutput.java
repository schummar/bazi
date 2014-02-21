package de.uni_augsburg.bazi.list;

import de.uni_augsburg.bazi.common.algorithm.VectorOutput;
import de.uni_augsburg.bazi.common.util.MList;

/**
* Created by Marco on 21.02.14.
*/
public interface ListOutput extends VectorOutput
{
	public MList<VectorOutput> subApportionments();
	public void subApportionments(MList<VectorOutput> subApportionments);
}
