package de.uni_augsburg.bazi.monoprop;

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


	/*private StringTable asStringTable(Collection<? extends MonopropInput.Party> parties, Collection<? extends BasicMethod.Output> outputs)
	{
		StringTable st = new StringTable();
		StringTable.Column names = st.col(0), votes = st.col(1), conditions = st.col(2);

		names.append(Resources.get("output.names"));
		votes.append(Resources.get("output.votes"));
		conditions.append(Resources.get("output.conditions"));

		for (MonopropInput.Party party : parties)
		{
			names.append(party.name());
			votes.append(party.votes());
			conditions.append(String.format("%s|%s..%s", party.dir(), party.min(), party.max()));
		}

		boolean divisor = outputs.stream().map(BasicMethod.Output::method).anyMatch(BasicMethod.Divisor.class::isInstance);
		boolean quota = outputs.stream().map(BasicMethod.Output::method).anyMatch(BasicMethod.Quota.class::isInstance);
		String label = divisor && quota ? "div_quo" : (divisor ? "div" : "quo");
		if (options.divisorFormat == Options.DivisorFormat.QUOTIENT)
		{
			names.append(String.format("%s (%s)", Resources.get("output.sum"), Resources.get("output.div_quo." + label)));
		}
		else
		{
			names.append(Resources.get("output.sum"));
			names.append(Resources.get("output." + options.divisorFormat.name().toLowerCase() + "." + label));
		}

		Real sum = parties.stream().map(MonopropInput.Party::votes).reduce(Real::add).get();
		Rational dirSum = parties.stream().map(MonopropInput.Party::dir).reduce(Int::add).get();
		Rational minSum = parties.stream().map(MonopropInput.Party::min).reduce(Int::add).get();
		Rational maxSum = parties.stream().map(MonopropInput.Party::max).reduce(Int::add).get();
		votes.append(sum);
		conditions.append(String.format("%s|%s..%s", dirSum, minSum, maxSum));

		for (BasicMethod.Output output : outputs)
			st.append(output.asStringTable(options.divisorFormat, options.tieFormat));

		return options.orientation == Options.Orientation.VERTICAL ? st : st.transposed();
	}*/


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
