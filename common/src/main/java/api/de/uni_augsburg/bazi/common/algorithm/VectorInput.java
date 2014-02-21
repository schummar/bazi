package de.uni_augsburg.bazi.common.algorithm;

import de.uni_augsburg.bazi.common.Data;
import de.uni_augsburg.bazi.common.util.MList;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Real;

/**
* Created by Marco on 21.02.14.
*/
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
