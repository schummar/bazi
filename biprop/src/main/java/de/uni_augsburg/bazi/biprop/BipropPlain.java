package de.uni_augsburg.bazi.biprop;

import de.schummar.castable.Attribute;
import de.schummar.castable.Data;
import de.uni_augsburg.bazi.common.Resources;
import de.uni_augsburg.bazi.common.StringTable;
import de.uni_augsburg.bazi.common.algorithm.Algorithm;
import de.uni_augsburg.bazi.common.plain.DivisorFormat;
import de.uni_augsburg.bazi.common.plain.PlainOptions;
import de.uni_augsburg.bazi.common.util.CollectionHelper;
import de.uni_augsburg.bazi.common_matrix.MatrixPlain;
import de.uni_augsburg.bazi.divisor.Divisor;
import de.uni_augsburg.bazi.divisor.DivisorData;
import de.uni_augsburg.bazi.divisor.DivisorPlain;
import de.uni_augsburg.bazi.divisor.RoundingFunction;
import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Real;
import javafx.beans.property.Property;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static de.uni_augsburg.bazi.common.algorithm.VectorData.Party;

/** A PlainSupplier that generates plain data for biproportional algorithms on request. */
public class BipropPlain extends MatrixPlain
{
	/** PlainOptions for bipropotional methods. */
	public interface BipropPlainOptions extends PlainOptions
	{
		/**
		 * Whether to sort the super parties according to the number of seats
		 * they aquired in the super apportionment.
		 * @return true iff the super parties are sorted according to the number of seats
		 * they aquired in the super apportionment.
		 */
		@Attribute(def = "true") Property<Boolean> sortSuperProperty();
		default Boolean sortSuper() { return sortSuperProperty().getValue(); }
		default void sortSuper(Boolean v) { sortSuperProperty().setValue(v); }
	}


	public static final StringTable.Key DIVISOR = new StringTable.Key();

	protected final BipropData data;
	protected final Algorithm<? extends DivisorData> Super;

	/**
	 * Constructor with initializers.
	 * @param data the biproportional result to produce plain data for.
	 * @param options options.
	 * @param Super the super method.
	 * @param sub the name of the divisor method used for the main apportionment.
	 */
	public BipropPlain(BipropData data, PlainOptions options, Algorithm<? extends DivisorData> Super, String sub)
	{
		super(data, options, sub);
		this.Super = Super;
		this.data = data;
	}


	public List<StringTable> get()
	{
		boolean sorted = options.cast(BipropPlainOptions.class).sortSuper();
		if (sorted)
			data.superApportionment().parties().sort(
				Comparator.<Party, Real>comparing(Party::votes)
					.<Int>thenComparing((Function<Party, Int>) Party::seats)
					.reversed()
			);

		List<StringTable> tables = new ArrayList<>();

		List<StringTable> dTables = Super.plainFormatter().apply(data.superApportionment(), options);
		dTables.get(0).titles().clear();
		dTables.get(0).titles().add(Resources.get(sorted ? "output.super_sorted" : "output.super"));
		tables.addAll(dTables);

		tables.get(0).titles().add(0, data.name());

		tables.add(getBipropTable());
		return tables;
	}


	/**
	 * Returns the table that summarizes the biproportional apportionment.
	 * @return the table that summarizes the biproportional apportionment.
	 */
	public StringTable getBipropTable()
	{
		StringTable table = new StringTable();
		table.titles().add(Resources.get("output.biprop_table", "?"));
		firstColumn(table.col(FIRST));
		seatSumColumn(table.col(SEATSUM));
		table.append(getParts());
		divisorColumn(table.col(DIVISOR));
		return table;
	}


	@Override public void firstColumn(StringTable.Column col)
	{
		super.firstColumn(col);
		col.set(Resources.get("output.div_quo.div"));
	}


	@Override public StringTable getPart(Object key)
	{
		StringTable table = new StringTable();

		List<Party> parties = parties(key).stream()
			.map(p -> p.copy().cast(Party.class))
			.collect(Collectors.toList());

		DivisorData out = Data.create(DivisorData.class);
		parties.forEach(
			p -> {
				out.parties().add(null);
				out.parties().get(out.parties().size() - 1).merge(p);
			}
		);
		out.divisor(colDivisor(key));
		PlainOptions opt = options.copy().cast(PlainOptions.class);
		opt.voteLabel(label(key));
		opt.divisorFormat(divisorFormat(key instanceof DivisorData));
		StringTable part = new Part(out, rowDivisors(key instanceof DivisorData)).get().get(0);
		table.append(part);

		return table;
	}


	/**
	 * Returns the divisor of a column.
	 * @param key the column key. Either an instance of DivisorOutput (=district) or a party name.
	 * @return the divisor of a column.
	 */
	public Divisor colDivisor(Object key)
	{
		return key instanceof DivisorData
			? ((DivisorData) key).divisor()
			: data.partyDivisors().get(key.toString());
	}


	/**
	 * Returns a list of all rowDivisors.
	 * @param forDistrict whether the cloumns represent districts.
	 * @return a list of all rowDivisors.
	 */
	public List<Divisor> rowDivisors(boolean forDistrict)
	{
		return forDistrict
			? names().stream().map(n -> data.partyDivisors().get(n)).collect(Collectors.toList())
			: data.districts().stream().map(d -> d.divisor()).collect(Collectors.toList());
	}


	/**
	 * Returns the divisor format to use for the parts.
	 * @param forDistrict whether the cloumns represent districts.
	 * @return the divisor format to use for the parts.
	 */
	public DivisorFormat divisorFormat(boolean forDistrict)
	{
		if (forDistrict) return options.divisorFormat();
		switch (options.divisorFormat())
		{
			case QUOTIENTS:
				return DivisorFormat.QUOTIENTS;
			case MULT:
			case MULT_INTERVAL:
				return DivisorFormat.MULT;
			case DIV_SPLIT:
			case INTERVAL:
			default:
				return DivisorFormat.DIV_SPLIT;
		}
	}


	/**
	 * Fill the column with the seats sums of each row.
	 * @param col the column that will be filled.
	 */
	public void seatSumColumn(StringTable.Column col)
	{
		col.add("");

		List<Int> seats = seats(options.orientation().matrixVertical());
		col.add(seats.stream().reduce(Int::add).orElse(BMath.ZERO).toString());
		seats.forEach(s -> col.add(s.toString()));
	}


	/**
	 * Fill the column with the divisors of each row.
	 * @param col the column that will be filled.
	 */
	public void divisorColumn(StringTable.Column col)
	{
		PlainOptions opt = options.copy().cast(PlainOptions.class);
		opt.divisorFormat(divisorFormat(!options.orientation().matrixVertical()));
		col.add(DivisorPlain.divisorLabel(options));
		col.add(String.format("[%s]", vectorName));
		rowDivisors(options.orientation().matrixVertical()).forEach(d -> col.add(DivisorPlain.divisor(d, opt)));
	}


	@Override public List<String> names()
	{
		return data.superApportionment().parties().stream()
			.map(Party::name)
			.collect(Collectors.toList());
	}


	/** One part for each district/party. Consists of columns for votes, seats, quotients. */
	public class Part extends DivisorPlain
	{
		protected final List<Divisor> rowDivisors;

		/**
		 * Constructor with initializers.
		 * @param output the divisor data to produce plain data for.
		 * @param rowDivisors a list of divisors for each row.
		 */
		public Part(DivisorData output, List<Divisor> rowDivisors)
		{
			super(output, BipropPlain.this.options, RoundingFunction.DIV_STD, vectorName);
			this.rowDivisors = rowDivisors;
		}
		@Override public void partyColumn(StringTable.Column col)
		{
		}
		@Override public void voteColumn(StringTable.Column col)
		{
			super.voteColumn(col);
			col.add(1, "");
			col.remove();
		}
		@Override public void resultColumn(StringTable.Column col)
		{
			super.resultColumn(col);
			if (options.divisorFormat() == DivisorFormat.QUOTIENTS)
			{
				col.add(1, col.get());
				col.set(divisor(data.divisor(), options));
			}
			else
			{
				col.add(1, col.get(col.size() - 2));
				col.remove(col.size() - 2);
			}
		}
		@Override public void quotientColumn(StringTable.Column col)
		{
			CollectionHelper.forEachPair(data.parties(), rowDivisors, (p, d) -> p.votes(p.votes().div(d.nice())));
			super.quotientColumn(col);
			col.add(1, "");
			col.remove();
		}
	}
}
