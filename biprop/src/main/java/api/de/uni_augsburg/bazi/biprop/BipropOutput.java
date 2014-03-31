package de.uni_augsburg.bazi.biprop;

import de.uni_augsburg.bazi.common.algorithm.MatrixOutput;
import de.uni_augsburg.bazi.divisor.Divisor;
import de.uni_augsburg.bazi.divisor.DivisorOutput;

import java.util.List;
import java.util.Map;

/** Output of a biproportional method. */
public interface BipropOutput extends MatrixOutput
{
	/**
	 * The output of the super apportionment.
	 * @return the output of the super apportionment.
	 */
	DivisorOutput superApportionment();

	/**
	 * The output of the super apportionment.
	 * @param superApportionment the output of the super apportionment.
	 */
	void superApportionment(DivisorOutput superApportionment);


	/** The party divisors.
	 * @return the party divisors. */
	Map<String, Divisor> partyDivisors();

	/** The party divisors.
	 * @param partyDivisors the party divisors. */
	void partyDivisors(Map<String, Divisor> partyDivisors);


	@Override List<? extends DivisorOutput> districts();
}
