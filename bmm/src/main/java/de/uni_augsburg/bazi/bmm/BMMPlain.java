package de.uni_augsburg.bazi.bmm;

import de.schummar.castable.Data;
import de.uni_augsburg.bazi.common.Resources;
import de.uni_augsburg.bazi.common.StringTable;
import de.uni_augsburg.bazi.common.algorithm.VectorData;
import de.uni_augsburg.bazi.common.plain.DivisorFormat;
import de.uni_augsburg.bazi.common.plain.PlainOptions;
import de.uni_augsburg.bazi.common_vector.VectorPlain;

import java.util.List;

/** A PlainSupplier that generates plain data for the bmm algorithm on request. */
public class BMMPlain
{
	protected final BMMAlgorithm algorithm;
	protected final VectorData data;
	protected final PlainOptions options;

	/**
	 * Constructor with initializers.
	 * @param algorithm the used algorithm.
	 * @param data the result to produce plain data for.
	 * @param options
	 */
	public BMMPlain(BMMAlgorithm algorithm, VectorData data, PlainOptions options)
	{
		this.data = data;
		this.algorithm = algorithm;
		this.options = options;
	}


	public List<StringTable> get()
	{
		List<StringTable> tables = algorithm.method().plainFormatter().apply(data, options);
		if (options.divisorFormat() == DivisorFormat.QUOTIENTS)
			modifyQuotientColumn(tables.get(0).col(VectorPlain.QUTIENT));
		return tables;
	}


	/**
	 * Modifies the quotient column. Adds the base seats to the quotients.
	 * @param col the column that will be filled.
	 */
	public void modifyQuotientColumn(StringTable.Column col)
	{
		List<String> quotients = col.subList(1, col.size() - 1);
		for (int i = 0; i < data.parties().size(); i++)
		{
			String s = String.format("%s+%s", algorithm.base(), quotients.get(i));
			if (data.parties().get(i).conditionUsed()) s += Resources.get("data.");
			quotients.set(i, s);
		}
	}
}
