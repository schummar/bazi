package de.uni_augsburg.bazi.quota;

import de.uni_augsburg.bazi.common.algorithm.VectorOutput;
import de.uni_augsburg.bazi.math.Real;

/** Output of the QuotaAlgorithm. */
public interface QuotaOutput extends VectorOutput
{
	/**
	 * The quota.
	 * @return the quota.
	 */
	public Real quota();

	/**
	 * The quota.
	 * @param quota the quota.
	 */
	public void quota(Real quota);
}
