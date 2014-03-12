package de.uni_augsburg.bazi.divisor;

import de.uni_augsburg.bazi.common.Resources;
import de.uni_augsburg.bazi.common.StringTable;
import de.uni_augsburg.bazi.common.plain.DivisorFormat;
import de.uni_augsburg.bazi.common.plain.PlainOptions;
import de.uni_augsburg.bazi.common_vector.VectorPlain;
import de.uni_augsburg.bazi.math.Real;

/**
 * Created by Marco on 06.03.14.
 */
public class DivisorPlain extends VectorPlain
{
	protected final DivisorOutput output;
	protected final RoundingFunction r;
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
			quotientColumn(col.inserBefore(), options);
	}

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
