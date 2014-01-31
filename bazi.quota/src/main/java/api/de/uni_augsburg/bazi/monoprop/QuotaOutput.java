package de.uni_augsburg.bazi.monoprop;

import de.uni_augsburg.bazi.math.Rational;

public class QuotaOutput extends MonopropOutput
{
	Rational quota;

	public QuotaOutput() { }

	public QuotaOutput(MonopropInput input)
	{
		super(input);
	}

	public Rational quota() { return quota; }
}
