package de.uni_augsburg.bazi.common_vector;

import de.uni_augsburg.bazi.common.Resources;
import de.uni_augsburg.bazi.common.StringTable;
import de.uni_augsburg.bazi.common.algorithm.VectorData;
import de.uni_augsburg.bazi.common.plain.Orientation;
import de.uni_augsburg.bazi.common.plain.PlainOptions;
import de.uni_augsburg.bazi.common.plain.PlainSupplier;
import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Real;

import java.util.Arrays;
import java.util.List;

/** A PlainSupplier that generates plain output for vector algorithms on request. */
public class VectorPlain implements PlainSupplier
{
	public static final StringTable.Key
		PARTY = new StringTable.Key(),
		VOTE = new StringTable.Key(),
		QUTIENT = new StringTable.Key(),
		CONDITION = new StringTable.Key(),
		SEAT = new StringTable.Key();

	protected final VectorData output;
	protected final String name;

	/**
	 * Constructor with initializers.
	 * @param output the result to produce plain output for.
	 * @param name the name of the vector method used for the apportionment.
	 */
	public VectorPlain(VectorData output, String name)
	{
		this.output = output;
		this.name = name;
	}


	@Override public List<StringTable> get(PlainOptions options)
	{
		StringTable table = new StringTable();

		if (output.name() != null)
			table.titles().add(output.name());

		Real sum = output.parties().stream()
			.map(VectorData.Party::votes)
			.reduce(Real::add).orElse(BMath.ZERO);
		Real half = sum.div(2);
		output.parties().stream()
			.filter(p -> p.votes().compareTo(half) > 0)
			.findAny().ifPresent(p -> table.titles().add(Resources.get("output.absolute_majority", p.name(), p.votes(), sum)));

		partyColumn(table.col(PARTY), options);
		voteColumn(table.col(VOTE), options);
		conditionColumn(table.col(CONDITION), options);
		resultColumn(table.col(SEAT), options);

		if (options.orientation() == Orientation.HORIZONTAL
			|| options.orientation() == Orientation.HORVER)
			return Arrays.asList(table.transposed());
		return Arrays.asList(table);
	}


	/**
	 * Fills a column with the party names.
	 * @param col the column that will be filled.
	 * @param options output options.
	 */
	public void partyColumn(StringTable.Column col, PlainOptions options)
	{
		if (options.nameLabel() != null) col.add(options.nameLabel());
		else col.add(Resources.get("output.names"));

		output.parties().forEach(p -> col.add(p.name()));

		col.add(Resources.get("output.sum"));
	}


	/**
	 * Fills a column with the party votes.
	 * @param col the column that will be filled.
	 * @param options output options.
	 */
	public void voteColumn(StringTable.Column col, PlainOptions options)
	{
		if (options.voteLabel() != null) col.add(options.voteLabel());
		else col.add(Resources.get("output.votes"));

		output.parties().forEach(p -> col.add(p.votes().precision(options.maxDigits()).toString()));

		Real sum = output.parties().stream()
			.map(VectorData.Party::votes)
			.reduce(Real::add).orElse(BMath.ZERO);
		col.add(sum.precision(options.maxDigits()).toString());
	}


	/**
	 * Fills a column with the party conditions.
	 * @param col the column that will be filled.
	 * @param options output options.
	 */
	public void conditionColumn(StringTable.Column col, PlainOptions options)
	{
		col.add(Resources.get("output.conditions"));
		output.parties().forEach(p -> col.add(String.format("%s..%s", p.min(), p.max())));

		Int minSum = output.parties().stream()
			.map(VectorData.Party::min)
			.reduce(Int::add).orElse(BMath.ZERO);
		Int maxSum = output.parties().stream()
			.map(VectorData.Party::max)
			.reduce(Int::add).orElse(BMath.ZERO);
		col.add(String.format("%s..%s", minSum, maxSum));

		if (minSum.equals(BMath.ZERO) && maxSum.equals(BMath.INF)) col.delete();
	}


	/**
	 * Fills a column with the party seats.
	 * @param col the column that will be filled.
	 * @param options output options.
	 */
	public void resultColumn(StringTable.Column col, PlainOptions options)
	{
		col.add(name);
		output.parties().forEach(p -> col.add(p.seats().toString() + p.uniqueness().toString()));

		Int sum = output.parties().stream()
			.map(VectorData.Party::seats)
			.reduce(Int::add).orElse(BMath.ZERO);
		col.add(sum.toString());
	}
}
