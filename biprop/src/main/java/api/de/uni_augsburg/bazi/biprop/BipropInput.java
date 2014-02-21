package de.uni_augsburg.bazi.biprop;



import de.uni_augsburg.bazi.common.algorithm.VectorInput;

import java.util.List;

public interface BipropInput
{
	public List<? extends District> districts();

	public interface District extends VectorInput
	{
		public String name();
	}
}
