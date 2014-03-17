package de.uni_augsburg.bazi.common.algorithm;

public class Options
{
	private long precision = 20;
	public Options(long precision)
	{
		this.precision = precision;
	}
	public Options()
	{ }


	public long precision()
	{
		return precision;
	}
	public void precision(long precision)
	{
		this.precision = precision;
	}
}
