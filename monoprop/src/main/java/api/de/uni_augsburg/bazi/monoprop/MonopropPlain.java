package de.uni_augsburg.bazi.monoprop;

import de.uni_augsburg.bazi.common.plain.Orientation;
import de.uni_augsburg.bazi.common.plain.PlainOptions;
import de.uni_augsburg.bazi.common.Resources;
import de.uni_augsburg.bazi.common.StringTable;
import de.uni_augsburg.bazi.common.algorithm.VectorOutput;
import de.uni_augsburg.bazi.common.plain.PlainSupplier;
import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Real;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

/**
 * Created by Marco on 06.03.14.
 */
public class MonopropPlain implements PlainSupplier
{
	protected final VectorOutput output;
	public MonopropPlain(VectorOutput output)
	{
		this.output = output;
	}


	@Override public List<StringTable> get(PlainOptions options)
	{
		StringTable table = new StringTable();
		partyColumn(table.col(), options);
		voteColumn(table.col(), options);
		conditionColumn(table.col(), options);
		resultColumn(table.col(), options);

		if (options.orientation() == Orientation.HORIZONTAL
			|| options.orientation() == Orientation.HORVER)
			table = table.transposed();
		return Arrays.asList(table);
	}


	public void partyColumn(StringTable.Column col, PlainOptions options)
	{
		col.add(Resources.get("output.names"));
		output.parties().forEach(p -> col.add(p.name()));
		col.add(Resources.get("output.sum"));
	}


	public void voteColumn(StringTable.Column col, PlainOptions options)
	{
		col.add(Resources.get("output.votes"));
		output.parties().forEach(p -> col.add(p.votes().toString()));

		Real sum = output.parties().stream()
			.map(VectorOutput.Party::votes)
			.reduce(Real::add).orElse(BMath.ZERO);
		col.add(sum.toString());
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
	}


	public void resultColumn(StringTable.Column col, PlainOptions options)
	{
		col.add(output.name());
		output.parties().forEach(p -> col.add(p.seats().toString() + p.uniqueness().toString()));

		Int sum = output.parties().stream()
			.map(VectorOutput.Party::seats)
			.reduce(Int::add).orElse(BMath.ZERO);
		col.add(sum.toString());
	}
}
