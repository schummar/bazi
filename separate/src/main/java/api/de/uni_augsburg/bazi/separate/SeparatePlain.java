package de.uni_augsburg.bazi.separate;

import de.schummar.castable.Data;
import de.uni_augsburg.bazi.common.Resources;
import de.uni_augsburg.bazi.common.StringTable;
import de.uni_augsburg.bazi.common.algorithm.MatrixData;
import de.uni_augsburg.bazi.common.algorithm.VectorData;
import de.uni_augsburg.bazi.common.plain.PlainOptions;
import de.uni_augsburg.bazi.common_matrix.MatrixPlain;
import de.uni_augsburg.bazi.common_vector.VectorPlain;
import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Real;

import java.util.ArrayList;
import java.util.List;

/** A PlainSupplier that generates plain output for the separate algorithm on request. */
public class SeparatePlain extends MatrixPlain
{
	public static final StringTable.Key
		VOTE_SUM = new StringTable.Key();

	/**
	 * @param data the output to produce plain output for.
	 * @param vectorName the display name of the method that was used for the district apportionments.
	 */
	public SeparatePlain(MatrixData data, String vectorName)
	{
		super(data, vectorName);
	}


	@Override public List<StringTable> get(PlainOptions options)
	{
		List<StringTable> tables = new ArrayList<>();
		output.districts().forEach(
			d -> {
				List<StringTable> dTables = null;//d.plain().get(options);
				dTables.get(0).titles().set(0, Resources.get("output.district", output.districts().indexOf(d) + 1, d.name()));
				tables.addAll(dTables);
			}
		);
		tables.get(0).titles().add(0, output.name());

		tables.add(getSummary(options));
		return tables;
	}


	/**
	 * Creates the summary table.
	 * @param options output options.
	 * @return the summary table.
	 */
	public StringTable getSummary(PlainOptions options)
	{
		StringTable table = new StringTable();
		table.titles().add(Resources.get("output.summary", output.districts().size(), vectorName));
		firstColumn(table.col(), options);
		table.append(getParts(options));
		voteSumColumn(table.col(), options);
		seatSumColumn(table.col(), options);
		return table;
	}

	@Override public StringTable getPart(Object key, PlainOptions options)
	{
		StringTable table = new StringTable();

		VectorData out = Data.create(VectorData.class);
		parties(key).forEach(
			p -> {
				out.parties().add(null);
				out.parties().get(out.parties().size() - 1).merge(p);
			}
		);
		PlainOptions opt = options.copy().cast(PlainOptions.class);
		opt.voteLabel(label(key));
		StringTable part = new VectorPlain(out, vectorName).get(opt).get(0);
		part.removeAll(VectorPlain.PARTY);
		part.cols().forEach(c -> c.add(1, ""));
		table.append(part);

		return table;
	}


	/**
	 * Fills a column with the vote sums of each row.
	 * @param col the column to be filled.
	 * @param options output options.
	 */
	public void voteSumColumn(StringTable.Column col, PlainOptions options)
	{
		col.add(Resources.get("output.sum"));
		col.add(Resources.get("output.votes"));

		List<Real> votes = votes(options.orientation().matrixVertical());
		votes.forEach(s -> col.add(s.toString()));
		col.add(votes.stream().reduce(Real::add).orElse(BMath.ZERO).toString());
	}


	/**
	 * Fills a column with the seat sums of each row.
	 * @param col the column to be filled.
	 * @param options output options.
	 */
	public void seatSumColumn(StringTable.Column col, PlainOptions options)
	{
		col.add(Resources.get("output.sum"));
		col.add(vectorName);

		List<Int> seats = seats(options.orientation().matrixVertical());
		seats.forEach(s -> col.add(s.toString()));
		col.add(seats.stream().reduce(Int::add).orElse(BMath.ZERO).toString());
	}
}
