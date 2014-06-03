package de.uni_augsburg.bazi.dir;

import de.uni_augsburg.bazi.common.Resources;
import de.uni_augsburg.bazi.common.StringTable;
import de.uni_augsburg.bazi.common.plain.PlainOptions;
import de.uni_augsburg.bazi.common.plain.PlainSupplier;
import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;

import java.util.List;

/** A PlainSupplier that generates plain output for the DirFilter on request. */
public class DirPlain implements PlainSupplier
{
	public static final StringTable.Key DIR = new StringTable.Key();


	private final DirData output;
	private PlainSupplier mainSupplier;

	/**
	 * Constructor with initializers.
	 * @param output the result to produce plain output for.
	 */
	public DirPlain(DirData output)
	{
		this.output = output;
		this.mainSupplier = null;//output.plain();
	}
	@Override public List<StringTable> get(PlainOptions options)
	{
		List<StringTable> tables = mainSupplier.get(options);
		if (output.parties().stream().anyMatch(p -> p.dir() != null && !p.dir().equals(0)))
			dirColumn(tables.get(0).col(DIR));
		return tables;
	}

	/**
	 * Fills a column with overhang seats for each party.
	 * @param col the column to fill.
	 */
	public void dirColumn(StringTable.Column col)
	{
		col.add(Resources.get("output.dir"));

		output.parties().forEach(p -> col.add(p.dir().sub(p.seats()).max(0).toString()));

		Int sum = output.parties().stream()
			.map(p -> p.dir().sub(p.seats()).max(0))
			.reduce(Int::add).orElse(BMath.ZERO);
		col.add(sum.toString());
	}
}
