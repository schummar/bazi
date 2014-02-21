package de.uni_augsburg.bazi.monoprop;

import de.uni_augsburg.bazi.common.algorithm.VectorOutput;
import de.uni_augsburg.bazi.math.Real;

public interface QuotaOutput extends VectorOutput
{
	public Real quota();
	public void quota(Real quota);
}
