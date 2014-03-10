package de.uni_augsburg.bazi.separate;

import de.uni_augsburg.bazi.common.Resources;
import de.uni_augsburg.bazi.common.StringTable;
import de.uni_augsburg.bazi.common.algorithm.MatrixOutput;
import de.uni_augsburg.bazi.common.algorithm.VectorOutput;
import de.uni_augsburg.bazi.common.data.Data;
import de.uni_augsburg.bazi.common.plain.Orientation;
import de.uni_augsburg.bazi.common.plain.PlainOptions;
import de.uni_augsburg.bazi.common.plain.PlainSupplier;
import de.uni_augsburg.bazi.common.util.CollectionHelper;
import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Real;
import de.uni_augsburg.bazi.monoprop.MonopropPlain;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static de.uni_augsburg.bazi.common.algorithm.VectorOutput.Party;

/**
 * Created by Marco on 10.03.14.
 */
public class SeparatePlain implements PlainSupplier
{
	private final MatrixOutput output;
	private final String vectorName;
	public SeparatePlain(MatrixOutput output, String vectorName)
	{
		this.output = output;
		this.vectorName = vectorName;
	}


	@Override public List<StringTable> get(PlainOptions options)
	{
		List<StringTable> tables = new ArrayList<>();
		output.districts().forEach(d -> tables.addAll(d.plain().get(options)));

		tables.add(getSummary(options));
		return tables;
	}


	public StringTable getSummary(PlainOptions options)
	{
		StringTable table = new StringTable();
		firstColumn(table.col(), options);
		table.append(getParts(options));
		voteSumColumn(table.col(), options);
		seatSumColumn(table.col(), options);
		return table;
	}


	public void firstColumn(StringTable.Column col, PlainOptions options)
	{
		col.add("");
		col.add("");

		if (options.orientation() == Orientation.VERTICAL || options.orientation() == Orientation.HORVER)
			names().forEach(col::add);
		else
			output.districts().stream().map(VectorOutput::name).forEach(col::add);

		col.add(Resources.get("output.sum"));
	}


	public StringTable getParts(PlainOptions options)
	{
		StringTable table = new StringTable();

		List<String> labels = labels(!options.orientation().matrixVertical());
		List<List<Party>> parties = parties(!options.orientation().matrixVertical());

		String label = options.voteLabel();
		CollectionHelper.forEach(
			labels, parties, (l, p) -> {
				VectorOutput out = Data.create(VectorOutput.class);
				out.parties(p);
				PlainOptions opt = options.copy(PlainOptions.class);
				opt.voteLabel(l);
				StringTable part = new MonopropPlain(out, vectorName).get(opt).get(0);
				part.col(0).delete();
				part.cols().forEach(c -> c.add(1, ""));
				table.append(part);
			}
		);
		options.voteLabel(label);

		return table;
	}


	public void voteSumColumn(StringTable.Column col, PlainOptions options)
	{
		col.add(Resources.get("output.sum"));
		col.add(Resources.get("output.votes"));

		List<Real> votes = votes(options.orientation().matrixVertical());
		votes.forEach(s -> col.add(s.toString()));
		col.add(votes.stream().reduce(Real::add).orElse(BMath.ZERO).toString());
	}


	public void seatSumColumn(StringTable.Column col, PlainOptions options)
	{
		col.add(Resources.get("output.sum"));
		col.add(vectorName);

		List<Int> seats = seats(options.orientation().matrixVertical());
		seats.forEach(s -> col.add(s.toString()));
		col.add(seats.stream().reduce(Int::add).orElse(BMath.ZERO).toString());
	}


	public List<String> names()
	{
		return new ArrayList<>(
			output.districts().stream()
				.flatMap(d -> d.parties().stream())
				.map(Party::name)
				.collect(Collectors.toCollection(LinkedHashSet::new))
		);
	}

	public Party party(String name, VectorOutput district)
	{
		Optional<? extends Party> optional = district.parties().stream()
			.filter(p -> p.name().equals(name)).findAny();
		if (optional.isPresent()) return optional.get();

		Party party = Data.create(Party.class);
		party.name(name);
		return party;
	}

	public List<List<Party>> parties(boolean namesFirst)
	{
		return namesFirst
			? CollectionHelper.createTable(names(), output.districts(), this::party)
			: CollectionHelper.createTable(output.districts(), names(), (d, n) -> party(n, d));
	}

	public List<Real> votes(boolean forNames)
	{
		return parties(forNames).stream()
			.map(
				row -> row.stream()
					.map(Party::votes)
					.reduce(Real::add).orElse(BMath.ZERO)
			)
			.collect(Collectors.toList());
	}

	public List<Int> seats(boolean forNames)
	{
		return parties(forNames).stream()
			.map(
				row -> row.stream()
					.map(Party::seats)
					.reduce(Int::add).orElse(BMath.ZERO)
			)
			.collect(Collectors.toList());
	}

	public List<String> labels(boolean names)
	{
		return names
			? names()
			: output.districts().stream().map(VectorOutput::name).collect(Collectors.toList());
	}
}
