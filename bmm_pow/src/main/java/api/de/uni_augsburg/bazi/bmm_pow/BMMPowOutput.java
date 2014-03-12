package de.uni_augsburg.bazi.bmm_pow;

import de.uni_augsburg.bazi.common.algorithm.OutputList;
import de.uni_augsburg.bazi.common.algorithm.VectorOutput;
import de.uni_augsburg.bazi.math.Real;

import java.util.List;

/**
 * Created by Marco on 12.03.2014.
 */
public interface BMMPowOutput extends OutputList
{
	@Override List<BMMPowResult> results();
	public interface BMMPowResult extends VectorOutput
	{
		Real power();
		void power(Real power);
	}
}
