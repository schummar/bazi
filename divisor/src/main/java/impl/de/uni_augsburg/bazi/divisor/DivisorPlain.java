package de.uni_augsburg.bazi.divisor;

import de.uni_augsburg.bazi.common.Resources;
import de.uni_augsburg.bazi.common.StringTable;
import de.uni_augsburg.bazi.common.plain.DivisorFormat;
import de.uni_augsburg.bazi.common.plain.PlainOptions;
import de.uni_augsburg.bazi.math.Real;
import de.uni_augsburg.bazi.monoprop.MonopropPlain;

/**
 * Created by Marco on 06.03.14.
 */
public class DivisorPlain extends MonopropPlain
{
	protected final DivisorOutput output;
	protected final RoundingFunction r;
	public DivisorPlain(DivisorOutput output, RoundingFunction r)
	{
		super(output);
		this.output = output;
		this.r = r;
	}

	@Override public void partyColumn(StringTable.Column col, PlainOptions options)
	{
		super.partyColumn(col, options);
		if (options.divisorFormat() != DivisorFormat.QUOTIENTS)
			col.add(Resources.get("output.div_quo.div"));
		else
		{
			col.set(String.format("%s (%s)", col.get(), Resources.get("output.div_quo.div")));
		}
	}

	@Override public void resultColumn(StringTable.Column col, PlainOptions options)
	{
		super.resultColumn(col, options);
		if (options.divisorFormat() != DivisorFormat.QUOTIENTS)
			col.add(divisor(options));
		else
			quotientColumn(col.inserBefore(), options);
	}

	public void quotientColumn(StringTable.Column col, PlainOptions options)
	{
		col.add(Resources.get("output.quotient"));
		output.parties().forEach(
			p -> {
				Real q = p.votes().div(output.divisor().nice());
				col.add(DivisorRoundingHelper.round(q, 3, 100, r).toString());
			}
		);
		col.add(String.format("(%s)", divisor(options)));
	}

	public String divisor(PlainOptions options)
	{
		switch (options.divisorFormat())
		{
			case INTERVAL:
				return String.format("[%s..%s]", output.divisor().min(), output.divisor().max());
			case MULT:
				return output.divisor().nice().inv().toString();
			case MULT_INTERVAL:
				return String.format("[%s..%s]", output.divisor().max().inv(), output.divisor().min().inv());
			case DIV_SPLIT:
			case QUOTIENTS:
			default:
				return output.divisor().nice().toString();
		}
	}
}