package de.uni_augsburg.bazi.divisor;

import de.uni_augsburg.bazi.common.Resources;
import de.uni_augsburg.bazi.common.StringTable;
import de.uni_augsburg.bazi.common.plain.DivisorFormat;
import de.uni_augsburg.bazi.common.plain.PlainOptions;
import de.uni_augsburg.bazi.common_vector.VectorPlain;
import de.uni_augsburg.bazi.math.Real;

/** A PlainSupplier that generates plain output for the divisor algorithm on request. */
public class DivisorPlain extends VectorPlain
{
	protected final DivisorOutput output;
	protected final RoundingFunction r;

	/**
	 * @param output the output to produce plain output for.
	 * @param r the rounding function the divisor algorithm used.
	 * @param name the display name of the algorithm.
	 */
	public DivisorPlain(DivisorOutput output, RoundingFunction r, String name)
	{
		super(output, name);
		this.output = output;
		this.r = r;
	}

	@Override public void partyColumn(StringTable.Column col, PlainOptions options)
	{
		super.partyColumn(col, options);
		if (options.divisorFormat() != DivisorFormat.QUOTIENTS)
			col.add(divisorLabel(options));
		else
		{
			col.set(String.format("%s (%s)", col.get(), divisorLabel(options)));
		}
	}

	@Override public void resultColumn(StringTable.Column col, PlainOptions options)
	{
		super.resultColumn(col, options);
		if (options.divisorFormat() != DivisorFormat.QUOTIENTS)
			col.add(divisor(output.divisor(), options));
		else
			quotientColumn(col.inserBefore(QUTIENT), options);
	}


	/**
	 * Fills a column with the quotients for each party.
	 * @param col the column to fill.
	 * @param options output options.
	 */
	public void quotientColumn(StringTable.Column col, PlainOptions options)
	{
		col.add(Resources.get("output.quotient"));
		output.parties().forEach(
			p -> {
				Real q = p.votes().div(output.divisor().nice());
				col.add(DivisorRoundingHelper.round(q, 1, options.maxDigits(), r).toString());
			}
		);
		col.add(String.format("(%s)", divisor(output.divisor(), options)));
	}


	/**
	 * Returns the Divisor as String.
	 * @param divisor the divisor.
	 * @param options output options.
	 * @return the Divisor as String.
	 */
	public static String divisor(Divisor divisor, PlainOptions options)
	{
		switch (options.divisorFormat())
		{
			case INTERVAL:
				return String.format(
					"[%s;%s]",
					divisor.min().precision(options.maxDigits()),
					divisor.max().precision(options.maxDigits())
				);
			case MULT:
				return divisor.niceMultiplier().precision(options.maxDigits()).toString();
			case MULT_INTERVAL:
				return String.format(
					"[%s;%s]",
					divisor.minMultiplier().precision(options.maxDigits()),
					divisor.maxMultiplier().precision(options.maxDigits())
				);
			case DIV_SPLIT:
			case QUOTIENTS:
			default:
				return divisor.nice().precision(options.maxDigits()).toString();
		}
	}


	/**
	 * Returns the divisor label.
	 * @param options output options.
	 * @return the divisor label.
	 */
	public static String divisorLabel(PlainOptions options)
	{
		switch (options.divisorFormat())
		{
			case DIV_SPLIT:
			case QUOTIENTS:
			default:
				return Resources.get("output.div_quo.div");
			case INTERVAL:
				return Resources.get("output.div_interval.div");
			case MULT:
				return Resources.get("output.mul.div");
			case MULT_INTERVAL:
				return Resources.get("output.mul_interval.div");
		}
	}
}
