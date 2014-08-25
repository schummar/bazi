package de.uni_augsburg.bazi.common.algorithm;

/** Collection of general options to be passed to each algorithm, subalgorithm, filter, etc. */
public class Options
{
	private long precision = 20;

	/**
	 * Constructor with initializers.
	 * @param precision the minimal precision (decimal places) for unexact calculations.
	 */
	public Options(long precision)
	{
		this.precision = precision;
	}


	/** Default Constructor */
	public Options()
	{}


	/**
	 * The minumum precision (decimal places) for unexact calculations.
	 * @return the minumum precision (decimal places) for unexact calculations.
	 */
	public long precision()
	{
		return precision;
	}

	/**
	 * The minumum precision (decimal places) for unexact calculations.
	 * @param precision the minumum precision (decimal places) for unexact calculations.
	 */
	public void precision(long precision)
	{
		this.precision = precision;
	}
}
