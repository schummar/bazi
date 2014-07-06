package de.uni_augsburg.bazi.common_vector;

import de.uni_augsburg.bazi.common.Resources;
import de.uni_augsburg.bazi.common.StringTable;
import de.uni_augsburg.bazi.common.algorithm.VectorData;
import de.uni_augsburg.bazi.common.plain.PlainOptions;
import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Real;

import java.util.Arrays;
import java.util.List;

/** A PlainSupplier that generates plain data for vector algorithms on request. */
public class VectorPlain
{
	public static final StringTable.Key
		PARTY = new StringTable.Key(),
		VOTE = new StringTable.Key(),
		QUTIENT = new StringTable.Key(),
		CONDITION = new StringTable.Key(),
		SEAT = new StringTable.Key();

	protected final VectorData data;
	protected final PlainOptions options;
	protected final String name;

	/**
	 * Constructor with initializers.
	 * @param output the result to produce plain data for.
	 * @param name the name of the vector method used for the apportionment.
	 */
	public VectorPlain(VectorData output, PlainOptions options, String name)
	{
		this.data = output;
		this.options = options;
		this.name = name;
	}


	public List<StringTable> get()
	{
		StringTable table = new StringTable();

		if (data.name() != null)
			table.titles().add(data.name());

		Real sum = data.parties().stream()
			.map(VectorData.Party::votes)
			.reduce(Real::add).orElse(BMath.ZERO);
		Real half = sum.div(2);
		data.parties().stream()
			.filter(p -> p.votes().compareTo(half) > 0)
			.findAny().ifPresent(p -> table.titles().add(Resources.get("output.absolute_majority", p.name(), p.votes(), sum)));

		partyColumn(table.col(PARTY));
		voteColumn(table.col(VOTE));
		conditionColumn(table.col(CONDITION));
		resultColumn(table.col(SEAT));

		table.setTransposed(!options.orientation().vectorVertical());
		return Arrays.asList(table);
	}


	/**
	 * Fills a column with the party names.
	 * @param col the column that will be filled.
	 */
	public void partyColumn(StringTable.Column col)
	{
		if (options.nameLabel() != null) col.add(options.nameLabel());
		else col.add(Resources.get("output.names"));

		data.parties().forEach(p -> col.add(p.name()));

		col.add(Resources.get("output.sum"));
	}


	/**
	 * Fills a column with the party votes.
	 * @param col the column that will be filled.
	 */
	public void voteColumn(StringTable.Column col)
	{
		if (options.voteLabel() != null) col.add(options.voteLabel());
		else col.add(Resources.get("output.votes"));

		data.parties().forEach(p -> col.add(p.votes().precision(options.maxDigits()).toString()));

		Real sum = data.parties().stream()
			.map(VectorData.Party::votes)
			.reduce(Real::add).orElse(BMath.ZERO);
		col.add(sum.precision(options.maxDigits()).toString());
	}


	/**
	 * Fills a column with the party conditions.
	 * @param col the column that will be filled.
	 */
	public void conditionColumn(StringTable.Column col)
	{
		col.add(Resources.get("output.conditions"));
		data.parties().forEach(p -> col.add(String.format("%s..%s", p.min(), p.max())));

		Int minSum = data.parties().stream()
			.map(VectorData.Party::min)
			.reduce(Int::add).orElse(BMath.ZERO);
		Int maxSum = data.parties().stream()
			.map(VectorData.Party::max)
			.reduce(Int::add).orElse(BMath.ZERO);
		col.add(String.format("%s..%s", minSum, maxSum));

		if (minSum.equals(BMath.ZERO) && maxSum.equals(BMath.INF)) col.delete();
	}


	/**
	 * Fills a column with the party seats.
	 * @param col the column that will be filled.
	 */
	public void resultColumn(StringTable.Column col)
	{
		col.add(name);
		data.parties().forEach(p -> col.add(p.seats().toString() + p.uniqueness().toString()));
		col.add(data.seats().toString());
	}
}
