package de.uni_augsburg.bazi.bmm_pow;

import de.uni_augsburg.bazi.common.algorithm.OutputList;
import de.uni_augsburg.bazi.common.algorithm.VectorOutput;
import de.uni_augsburg.bazi.divisor.DivisorOutput;
import de.uni_augsburg.bazi.math.Real;

import java.util.List;

/** Output iof the BMMPow Algorithm. */
public interface BMMPowOutput extends OutputList, VectorOutput
{
	@Override List<BMMPowResult> results();

	/** One possible result (for one possible power). */
	public interface BMMPowResult extends DivisorOutput
	{
		/**
		 * The power the votes were exponentiated with.
		 * @return the power the votes were exponentiated with.
		 */
		Real power();

		/**
		 * The power the votes were exponentiated with.
		 * @param power the power the votes were exponentiated with.
		 */
		void power(Real power);
	}
}
