package de.uni_augsburg.bazi.dir;

import de.uni_augsburg.bazi.common.Resources;
import de.uni_augsburg.bazi.common.StringTable;
import de.uni_augsburg.bazi.common.algorithm.Algorithm;
import de.uni_augsburg.bazi.common.algorithm.VectorData;
import de.uni_augsburg.bazi.common.plain.PlainOptions;
import de.uni_augsburg.bazi.common_vector.VectorPlain;
import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;

import java.util.List;

/** A PlainSupplier that generates plain data for the DirFilter on request. */
public class DirPlain
{
	public static final StringTable.Key DIR = new StringTable.Key(), OVERHANG = new StringTable.Key();


	private final DirData data;
	private final PlainOptions options;
	private final Algorithm<? extends VectorData> algorithm;

	/**
	 * Constructor with initializers.
	 * @param data the result to produce plain data for.
	 * @param options options.
	 * @param algorithm the algorithm.
	 */
	public DirPlain(DirData data, PlainOptions options, Algorithm<? extends VectorData> algorithm)
	{
		this.data = data;
		this.options = options;
		this.algorithm = algorithm;
	}
	public List<StringTable> get()
	{
		List<StringTable> tables = algorithm.plainFormatter().apply(data, options);
		if (data.parties().stream().anyMatch(p -> p.dir() != null && !p.dir().equals(0)))
		{
			dirColumn(tables.get(0).cols(VectorPlain.VOTE).get(0).insertAfter(DIR));
			overhangColumn(tables.get(0).col(OVERHANG));
		}
		return tables;
	}

	/**
	 * Fills a column with overhang seats for each party.
	 * @param col the column to fill.
	 */
	public void dirColumn(StringTable.Column col)
	{
		col.add("Dir");

		data.parties().forEach(p -> col.add(p.dir().toString()));

		Int sum = data.parties().stream()
			.map(DirData.Party::dir)
			.reduce(Int::add).orElse(BMath.ZERO);
		col.add(sum.toString());
	}

	public void overhangColumn(StringTable.Column col)
	{
		col.add(Resources.get("output.dir"));

		data.parties().forEach(p -> col.add(p.dir().sub(p.seats()).max(0).toString()));

		Int sum = data.parties().stream()
			.map(p -> p.dir().sub(p.seats()).max(0))
			.reduce(Int::add).orElse(BMath.ZERO);
		col.add(sum.toString());
	}
}
