package de.uni_augsburg.bazi.common_matrix;

import de.uni_augsburg.bazi.common.Resources;
import de.uni_augsburg.bazi.common.StringTable;
import de.uni_augsburg.bazi.common.algorithm.MatrixOutput;
import de.uni_augsburg.bazi.common.algorithm.VectorOutput;
import de.uni_augsburg.bazi.common.data.Data;
import de.uni_augsburg.bazi.common.plain.Orientation;
import de.uni_augsburg.bazi.common.plain.PlainOptions;
import de.uni_augsburg.bazi.common.plain.PlainSupplier;
import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Real;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Marco on 11.03.14.
 */
public abstract class MatrixPlain implements PlainSupplier
{
	public static final StringTable.Key
	FIRST=new StringTable.Key(),
	SEATSUM = new StringTable.Key();


	protected final MatrixOutput output;
	protected final String vectorName;
	protected MatrixPlain(MatrixOutput output, String vectorName)
	{
		this.output = output;
		this.vectorName = vectorName;
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
		if (options.orientation().matrixVertical())
			output.districts().forEach(d -> table.append(getPart(d, options)));
		else
			names().forEach(n -> table.append(getPart(n, options)));

		return table;
	}
	public abstract StringTable getPart(Object key, PlainOptions options);


	public List<String> names()
	{
		return new ArrayList<>(
			output.districts().stream()
				.flatMap(d -> d.parties().stream())
				.map(VectorOutput.Party::name)
				.collect(Collectors.toCollection(LinkedHashSet::new))
		);
	}

	public VectorOutput.Party party(String name, VectorOutput district)
	{
		Optional<? extends VectorOutput.Party> optional = district.parties().stream()
			.filter(p -> p.name().equals(name)).findAny();
		if (optional.isPresent()) return optional.get();

		VectorOutput.Party party = Data.create(VectorOutput.Party.class);
		party.name(name);
		return party;
	}

	public List<VectorOutput.Party> parties(Object key)
	{
		return key instanceof VectorOutput
			? names().stream().map(n -> party(n, (VectorOutput) key)).collect(Collectors.toList())
			: output.districts().stream().map(d -> party(key.toString(), d)).collect(Collectors.toList());
	}


	public String label(Object key)
	{
		return key instanceof VectorOutput
			? ((VectorOutput) key).name()
			: key.toString();
	}

	public List<Real> votes(boolean forNames)
	{
		return forNames
			? names().stream()
			.map(
				n -> output.districts().stream()
					.map(d -> party(n, d).votes())
					.reduce(Real::add).orElse(BMath.ZERO)
			)
			.collect(Collectors.toList())
			: output.districts().stream()
			.map(
				d -> names().stream()
					.map(n -> party(n, d).votes())
					.reduce(Real::add).orElse(BMath.ZERO)
			)
			.collect(Collectors.toList());
	}

	public List<Int> seats(boolean forNames)
	{
		return forNames
			? names().stream()
			.map(
				n -> output.districts().stream()
					.map(d -> party(n, d).seats())
					.reduce(Int::add).orElse(BMath.ZERO)
			)
			.collect(Collectors.toList())
			: output.districts().stream()
			.map(
				d -> names().stream()
					.map(n -> party(n, d).seats())
					.reduce(Int::add).orElse(BMath.ZERO)
			)
			.collect(Collectors.toList());
	}

}
