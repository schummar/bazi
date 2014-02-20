package de.uni_augsburg.bazi.vector;

import de.uni_augsburg.bazi.common.Data;
import de.uni_augsburg.bazi.common.MList;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Real;

public interface VectorInput extends Data
{
	public String name();
	public Int seats();
	public MList<? extends Party> parties();
	public interface Party extends Data
	{
		public String name();
		public Real votes();
		public Int min();
		public Int max();
	}
}
