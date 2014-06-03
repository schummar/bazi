package de.uni_augsburg.bazi.quota;

import de.uni_augsburg.bazi.common.Resources;
import de.uni_augsburg.bazi.common.StringTable;
import de.uni_augsburg.bazi.common.algorithm.VectorData;
import de.uni_augsburg.bazi.common.plain.DivisorFormat;
import de.uni_augsburg.bazi.common.plain.PlainOptions;
import de.uni_augsburg.bazi.common_vector.VectorPlain;
import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Interval;
import de.uni_augsburg.bazi.math.Real;

/** A PlainSupplier that generates plain output for the divisor algorithm on request. */
public class QuotaPlain extends VectorPlain
{
	private final QuotaData output;

	/**
	 * @param output the output to produce plain output for.
	 * @param name the display name of the algorithm.
	 */
	public QuotaPlain(QuotaData output, PlainOptions options, String name)
	{
		super(output, options, name);
		this.output = output;
	}

	@Override public void partyColumn(StringTable.Column col)
	{
		super.partyColumn(col);
		if (options.divisorFormat() != DivisorFormat.QUOTIENTS)
			col.add(quotaLabel(options));
		else
		{
			col.set(String.format("%s (%s)", col.get(), quotaLabel(options)));
		}
	}

	@Override public void resultColumn(StringTable.Column col)
	{
		super.resultColumn(col);
		if (options.divisorFormat() != DivisorFormat.QUOTIENTS)
			col.add(split(splitInterval()));
		else
			quotientColumn(col.inserBefore(QUTIENT));
	}


	/**
	 * Fills a column with the quotients for each party.
	 * @param col the column to fill.
	 */
	public void quotientColumn(StringTable.Column col)
	{
		col.add(Resources.get("output.quotient"));

		Interval split = splitInterval();
		output.parties().forEach(
			p -> {
				Real q = p.votes().div(output.quota());
				col.add(QuotaRoundingHelper.round(q, split, 3, 10).toString());
			}
		);

		col.add(String.format("(%s)", split(split)));
	}


	/**
	 * Returns the split interval as String.
	 * @param split the split interval.
	 * @return the split interval as String.
	 */
	public String split(Interval split)
	{
		switch (options.divisorFormat())
		{
			case INTERVAL:
				return String.format("[%s;%s]", splitToString(split.min()), splitToString(split.max()));
			case MULT:
				return "---";
			case MULT_INTERVAL:
				return "---";
			case DIV_SPLIT:
			case QUOTIENTS:
			default:
				return splitToString(split.nice());
		}
	}

	private String splitToString(Real r)
	{
		String rs = r.precision(10).toString();
		if (rs.startsWith("0.")) rs = rs.substring(1);
		return rs;
	}


	/**
	 * Calculates the split interval.
	 * @return the split interval.
	 */
	public Interval splitInterval()
	{
		Real min = BMath.ZERO, max = BMath.ONE;
		for (VectorData.Party p : output.parties())
		{
			Real q = p.votes().div(output.quota()), i = q.floor(), f = q.frac();
			if (p.seats().equals(i)) min = min.max(f);
			else max = max.min(f);
		}
		return Interval.of(min, max);
	}


	/**
	 * Returns the quota label.
	 * @param options output options.
	 * @return the quota label.
	 */
	public static String quotaLabel(PlainOptions options)
	{
		switch (options.divisorFormat())
		{
			case DIV_SPLIT:
			case QUOTIENTS:
			default:
				return Resources.get("output.div_quo.quo");
			case INTERVAL:
				return Resources.get("output.div_interval.quo");
			case MULT:
				return Resources.get("output.mul.quo");
			case MULT_INTERVAL:
				return Resources.get("output.mul_interval.quo");
		}
	}
}
