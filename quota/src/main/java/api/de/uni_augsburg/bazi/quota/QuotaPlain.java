package de.uni_augsburg.bazi.quota;

import de.uni_augsburg.bazi.common.Resources;
import de.uni_augsburg.bazi.common.StringTable;
import de.uni_augsburg.bazi.common.algorithm.VectorOutput;
import de.uni_augsburg.bazi.common.plain.DivisorFormat;
import de.uni_augsburg.bazi.common.plain.PlainOptions;
import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Interval;
import de.uni_augsburg.bazi.math.Real;
import de.uni_augsburg.bazi.monoprop.MonopropPlain;

/**
 * Created by Marco on 07.03.14.
 */
public class QuotaPlain extends MonopropPlain
{
	private final QuotaOutput output;
	public QuotaPlain(QuotaOutput output, String name)
	{
		super(output, name);
		this.output = output;
	}

	@Override public void partyColumn(StringTable.Column col, PlainOptions options)
	{
		super.partyColumn(col, options);
		if (options.divisorFormat() != DivisorFormat.QUOTIENTS)
			col.add(Resources.get("output.div_quo.quo"));
		else
		{
			col.set(String.format("%s (%s)", col.get(), Resources.get("output.div_quo.quo")));
		}
	}

	@Override public void resultColumn(StringTable.Column col, PlainOptions options)
	{
		super.resultColumn(col, options);
		if (options.divisorFormat() != DivisorFormat.QUOTIENTS)
			col.add(split(splitInterval(), options));
		else
			quotientColumn(col.inserBefore(), options);
	}

	public void quotientColumn(StringTable.Column col, PlainOptions options)
	{
		col.add(Resources.get("output.quotient"));

		Interval split = splitInterval();
		output.parties().forEach(
			p -> {
				Real q = p.votes().div(output.quota());
				col.add(QuotaRoundingHelper.round(q, split, 3, 10).toString());
			}
		);

		col.add(String.format("(%s)", split(split, options)));
	}

	public String split(Interval split, PlainOptions options)
	{
		switch (options.divisorFormat())
		{
			case INTERVAL:
				return String.format("[%s..%s]", splitToString(split.min()), splitToString(split.max()));
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

	public Interval splitInterval()
	{
		Real min = BMath.ZERO, max = BMath.ONE;
		for (VectorOutput.Party p : output.parties())
		{
			Real q = p.votes().div(output.quota()), i = q.floor(), f = q.frac();
			if (p.seats().equals(i)) min = min.max(f);
			else max = max.min(f);
		}
		return Interval.of(min, max);
	}
}
