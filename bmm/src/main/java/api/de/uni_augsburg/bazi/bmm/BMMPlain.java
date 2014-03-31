package de.uni_augsburg.bazi.bmm;

import de.uni_augsburg.bazi.common.Resources;
import de.uni_augsburg.bazi.common.StringTable;
import de.uni_augsburg.bazi.common.algorithm.VectorOutput;
import de.uni_augsburg.bazi.common.plain.DivisorFormat;
import de.uni_augsburg.bazi.common.plain.PlainOptions;
import de.uni_augsburg.bazi.common.plain.PlainSupplier;

import java.util.List;

/** A PlainSupplier that generates plain output for the bmm algorithm on request. */
public class BMMPlain implements PlainSupplier
{
	protected final VectorOutput output;
	protected final PlainSupplier sub;
	protected final BMMAlgorithm algo;

	/**
	 * Constructor with initializers.
	 * @param output the result to prduce plain output for.
	 * @param algo the used algorithm.
	 */
	public BMMPlain(VectorOutput output, BMMAlgorithm algo)
	{
		this.output = output;
		this.sub = output.plain();
		this.algo = algo;
	}


	@Override public List<StringTable> get(PlainOptions options)
	{
		List<StringTable> tables = sub.get(options);
		//tables.get(0).col(2).delete();
		if (options.divisorFormat() == DivisorFormat.QUOTIENTS)
			modifyQuotientColumn(tables.get(0).col(2), options);
		return tables;
	}


	/**
	 * Modifies the quotient column. Adds the base seats to the quotients.
	 * @param col the column that will be filled.
	 * @param options output options.
	 */
	public void modifyQuotientColumn(StringTable.Column col, PlainOptions options)
	{
		List<String> quotients = col.subList(1, col.size() - 1);
		for (int i = 0; i < output.parties().size(); i++)
		{
			String s = String.format("%s+%s", algo.base, quotients.get(i));
			if (output.parties().get(i).conditionUsed()) s += Resources.get("output.");
			quotients.set(i, s);
		}
	}
}
