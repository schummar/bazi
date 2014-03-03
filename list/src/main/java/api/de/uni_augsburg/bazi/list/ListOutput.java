package de.uni_augsburg.bazi.list;

import de.uni_augsburg.bazi.common.algorithm.VectorOutput;

import java.util.List;

/**
 * Created by Marco on 21.02.14.
 */
public interface ListOutput extends VectorOutput
{
	List<? extends Party> parties();

	public interface Party extends VectorOutput.Party, VectorOutput {}
}
