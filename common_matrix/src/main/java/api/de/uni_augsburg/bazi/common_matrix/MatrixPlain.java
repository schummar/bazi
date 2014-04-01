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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/** A PlainSupplier that generates plain output for matrix algorithms on request. */
public abstract class MatrixPlain implements PlainSupplier
{
	public static final StringTable.Key
		FIRST = new StringTable.Key(),
		SEATSUM = new StringTable.Key();


	protected final MatrixOutput output;
	protected final String vectorName;

	/**
	 * Constructor with initializers.
	 * @param output the result to produce plain output for.
	 * @param vectorName the name of the vector method used for the apportionment.
	 */
	protected MatrixPlain(MatrixOutput output, String vectorName)
	{
		this.output = output;
		this.vectorName = vectorName;
	}


	/**
	 * Fills the first column which contains the names of the parties/districts.
	 * @param col the column that will be filled.
	 * @param options output options.
	 */
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


	/**
	 * Returns one part (one or more columns) for each district/party.
	 * @param options output options.
	 * @return one part (one or more columns) for each district/party.
	 */
	public StringTable getParts(PlainOptions options)
	{
		StringTable table = new StringTable();
		if (options.orientation().matrixVertical())
			output.districts().forEach(d -> table.append(getPart(d, options)));
		else
			names().forEach(n -> table.append(getPart(n, options)));

		return table;
	}

	/**
	 * Returns the part (one or more columns) for a specific district/party.
	 * @param key the district/party.
	 * @param options output options.
	 * @return the part (one or more columns) for a specific district/party.
	 */
	public abstract StringTable getPart(Object key, PlainOptions options);


	/**
	 * Returns a list of all party names.
	 * @return a list of all party names.
	 */
	public List<String> names()
	{
		return new ArrayList<>(
			output.districts().stream()
				.flatMap(d -> d.parties().stream())
				.map(VectorOutput.Party::name)
				.collect(Collectors.toList())
		);
	}


	/**
	 * Returns the party with the given name in the given district.
	 * If no such party exists a dummy is returned.
	 * @param name the party name.
	 * @param district the district.
	 * @return the party with the given name in the given district.
	 */
	public VectorOutput.Party party(String name, VectorOutput district)
	{
		Optional<? extends VectorOutput.Party> optional = district.parties().stream()
			.filter(p -> p.name().equals(name)).findAny();
		if (optional.isPresent()) return optional.get();

		VectorOutput.Party party = Data.create(VectorOutput.Party.class);
		party.name(name);
		return party;
	}


	/**
	 * Returns all parties for the given district/party name.
	 * @param key the district/party.
	 * @return all parties for the given district/party name.
	 */
	public List<VectorOutput.Party> parties(Object key)
	{
		return key instanceof VectorOutput
			? names().stream().map(n -> party(n, (VectorOutput) key)).collect(Collectors.toList())
			: output.districts().stream().map(d -> party(key.toString(), d)).collect(Collectors.toList());
	}


	/**
	 * Returns the name of the given district/party.
	 * @param key the district/party.
	 * @return the name of the given district/party.
	 */
	public String label(Object key)
	{
		return key instanceof VectorOutput
			? ((VectorOutput) key).name()
			: key.toString();
	}


	/**
	 * Returns a list of the vote sums for each district/party.
	 * @param forNames if true the vote sums for the each party is returned.
	 * Else the votes sums for each district.
	 * @return a list of the vote sums for each district/party.
	 */
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


	/**
	 * Returns a list of the seat sums for each district/party.
	 * @param forNames if true the seat sums for the each party is returned.
	 * Else the seat sums for each district.
	 * @return a list of the seat sums for each district/party.
	 */
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
