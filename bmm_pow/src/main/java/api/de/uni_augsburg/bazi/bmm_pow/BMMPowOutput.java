package de.uni_augsburg.bazi.bmm_pow;

import de.uni_augsburg.bazi.common.algorithm.OutputList;
import de.uni_augsburg.bazi.common.algorithm.VectorOutput;
import de.uni_augsburg.bazi.divisor.DivisorOutput;
import de.uni_augsburg.bazi.math.Real;

import java.util.List;

public interface BMMPowOutput extends OutputList, VectorOutput
{
	@Override List<BMMPowResult> results();
	public interface BMMPowResult extends DivisorOutput
	{
		Real power();
		void power(Real power);
	}
}
