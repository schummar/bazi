package de.uni_augsburg.bazi.monoprop;

import de.uni_augsburg.bazi.math.Real;

public class QuotaOutput extends MonopropOutput
{
	Real quota;

	public QuotaOutput() { }

	public QuotaOutput(MonopropInput input)
	{
		super(input);
	}

	public Real quota() { return quota; }
}
