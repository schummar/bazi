package de.uni_augsburg.bazi.common_vector;

import de.uni_augsburg.bazi.common.Resources;
import de.uni_augsburg.bazi.common.StringTable;
import de.uni_augsburg.bazi.common.algorithm.VectorOutput;
import de.uni_augsburg.bazi.common.plain.Orientation;
import de.uni_augsburg.bazi.common.plain.PlainOptions;
import de.uni_augsburg.bazi.common.plain.PlainSupplier;
import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Real;

import java.util.Arrays;
import java.util.List;

public class VectorPlain implements PlainSupplier
{
	public static final StringTable.Key
		PARTY = new StringTable.Key(),
		VOTE = new StringTable.Key(),
		QUTIENT = new StringTable.Key(),
		CONDITION = new StringTable.Key(),
		SEAT = new StringTable.Key();

	protected final VectorOutput output;
	protected final String name;
	public VectorPlain(VectorOutput output, String name)
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
			.map(VectorOutput.Party::votes)
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


	public void partyColumn(StringTable.Column col, PlainOptions options)
	{
		if (options.nameLabel() != null) col.add(options.nameLabel());
		else col.add(Resources.get("output.names"));

		output.parties().forEach(p -> col.add(p.name()));

		col.add(Resources.get("output.sum"));
	}


	public void voteColumn(StringTable.Column col, PlainOptions options)
	{
		if (options.voteLabel() != null) col.add(options.voteLabel());
		else col.add(Resources.get("output.votes"));

		output.parties().forEach(p -> col.add(p.votes().precision(options.maxDigits()).toString()));

		Real sum = output.parties().stream()
			.map(VectorOutput.Party::votes)
			.reduce(Real::add).orElse(BMath.ZERO);
		col.add(sum.precision(options.maxDigits()).toString());
	}


	public void conditionColumn(StringTable.Column col, PlainOptions options)
	{
		col.add(Resources.get("output.conditions"));
		output.parties().forEach(p -> col.add(String.format("%s..%s", p.min(), p.max())));

		Int minSum = output.parties().stream()
			.map(VectorOutput.Party::min)
			.reduce(Int::add).orElse(BMath.ZERO);
		Int maxSum = output.parties().stream()
			.map(VectorOutput.Party::max)
			.reduce(Int::add).orElse(BMath.ZERO);
		col.add(String.format("%s..%s", minSum, maxSum));

		if (minSum.equals(BMath.ZERO) && maxSum.equals(BMath.INF)) col.delete();
	}


	public void resultColumn(StringTable.Column col, PlainOptions options)
	{
		col.add(name);
		output.parties().forEach(p -> col.add(p.seats().toString() + p.uniqueness().toString()));

		Int sum = output.parties().stream()
			.map(VectorOutput.Party::seats)
			.reduce(Int::add).orElse(BMath.ZERO);
		col.add(sum.toString());
	}
}
