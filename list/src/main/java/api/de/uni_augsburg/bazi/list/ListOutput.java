package de.uni_augsburg.bazi.list;

import de.uni_augsburg.bazi.common.algorithm.VectorOutput;
import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;

import java.util.ArrayList;
import java.util.List;

public interface ListOutput extends VectorOutput
{
	List<? extends Party> parties();

	public interface Party extends VectorOutput.Party, VectorOutput
	{
		@Override default Int seats() { return BMath.ZERO; }
		@Override default List<ListOutput.Party> parties() { return new ArrayList<>(); }
	}
}
