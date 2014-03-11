package de.uni_augsburg.bazi.biprop;

import de.uni_augsburg.bazi.common.algorithm.MatrixOutput;
import de.uni_augsburg.bazi.divisor.Divisor;
import de.uni_augsburg.bazi.divisor.DivisorOutput;

import java.util.List;
import java.util.Map;

public interface BipropOutput extends MatrixOutput
{
	DivisorOutput superApportionment();
	@Override List<? extends District> districts();
	Map<String, Divisor> partyDivisors();

	void superApportionment(DivisorOutput superApportionment);
	void partyDivisors(Map<String, Divisor> partyDivisors);

	public interface District extends DivisorOutput
	{
		@Override List<? extends Party> parties();
	}
}
