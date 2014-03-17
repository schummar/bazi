package de.uni_augsburg.bazi.biprop;

import de.uni_augsburg.bazi.common.Resources;
import de.uni_augsburg.bazi.common.StringTable;
import de.uni_augsburg.bazi.common.data.Data;
import de.uni_augsburg.bazi.common.data.Default;
import de.uni_augsburg.bazi.common.plain.DivisorFormat;
import de.uni_augsburg.bazi.common.plain.PlainOptions;
import de.uni_augsburg.bazi.common.util.CollectionHelper;
import de.uni_augsburg.bazi.common_matrix.MatrixPlain;
import de.uni_augsburg.bazi.divisor.Divisor;
import de.uni_augsburg.bazi.divisor.DivisorOutput;
import de.uni_augsburg.bazi.divisor.DivisorPlain;
import de.uni_augsburg.bazi.divisor.RoundingFunction;
import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Real;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static de.uni_augsburg.bazi.common.algorithm.VectorOutput.Party;

public class BipropPlain extends MatrixPlain
{
	public static final StringTable.Key DIVISOR = new StringTable.Key();

	protected final BipropOutput output;
	protected final String sub;
	public BipropPlain(BipropOutput output, String Super, String sub)
	{
		super(output, sub);
		this.output = output;
		this.sub = sub;
	}


	@Override public List<StringTable> get(PlainOptions options)
	{
		boolean sorted = options.cast(BipropPlainOptions.class).sortSuper();
		if (sorted)
			output.superApportionment().parties().sort(
				Comparator.<Party, Real>comparing(Party::votes)
					.<Int>thenComparing((Function<Party, Int>) Party::seats)
					.reversed()
			);

		List<StringTable> tables = new ArrayList<>();

		List<StringTable> dTables = output.superApportionment().plain().get(options);
		dTables.get(0).titles().set(0, Resources.get(sorted ? "output.super_sorted" : "output.super"));
		tables.addAll(dTables);

		tables.get(0).titles().add(0, output.name());

		tables.add(getBipropTable(options));
		return tables;
	}


	public StringTable getBipropTable(PlainOptions options)
	{
		StringTable table = new StringTable();
		table.titles().add(Resources.get("output.biprop_table", "?"));
		firstColumn(table.col(FIRST), options);
		seatSumColumn(table.col(SEATSUM), options);
		table.append(getParts(options));
		divisorColumn(table.col(DIVISOR), options);
		return table;
	}


	@Override public void firstColumn(StringTable.Column col, PlainOptions options)
	{
		super.firstColumn(col, options);
		col.set(Resources.get("output.div_quo.div"));
	}


	@Override public StringTable getPart(Object key, PlainOptions options)
	{
		StringTable table = new StringTable();

		List<Party> parties = parties(key).stream()
			.map(p -> p.copy().cast(Party.class))
			.collect(Collectors.toList());

		DivisorOutput out = Data.create(DivisorOutput.class);
		out.parties(parties);
		out.divisor(colDivisor(key));
		PlainOptions opt = options.copy(PlainOptions.class);
		opt.voteLabel(label(key));
		opt.divisorFormat(divisorFormat(key instanceof DivisorOutput, options));
		StringTable part = new Part(out, RoundingFunction.DIV_STD, vectorName, rowDivisors(key instanceof DivisorOutput)).get(opt).get(0);
		table.append(part);

		return table;
	}

	public Divisor colDivisor(Object key)
	{
		return key instanceof DivisorOutput
			? ((DivisorOutput) key).divisor()
			: output.partyDivisors().get(key.toString());
	}

	public List<Divisor> rowDivisors(boolean forDistrict)
	{
		return forDistrict
			? names().stream().map(n -> output.partyDivisors().get(n)).collect(Collectors.toList())
			: output.districts().stream().map(d -> d.divisor()).collect(Collectors.toList());
	}

	public DivisorFormat divisorFormat(boolean forDistrict, PlainOptions options)
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


	public void seatSumColumn(StringTable.Column col, PlainOptions options)
	{
		col.add("");

		List<Int> seats = seats(options.orientation().matrixVertical());
		col.add(seats.stream().reduce(Int::add).orElse(BMath.ZERO).toString());
		seats.forEach(s -> col.add(s.toString()));
	}


	public void divisorColumn(StringTable.Column col, PlainOptions options)
	{
		PlainOptions opt = options.copy(PlainOptions.class);
		opt.divisorFormat(divisorFormat(!options.orientation().matrixVertical(), options));
		col.add(DivisorPlain.divisorLabel(options));
		col.add(String.format("[%s]", vectorName));
		rowDivisors(options.orientation().matrixVertical()).forEach(d -> col.add(DivisorPlain.divisor(d, opt)));
	}


	public interface BipropPlainOptions extends PlainOptions
	{
		@Default("true") boolean sortSuper();
	}

	@Override public List<String> names()
	{
		return output.superApportionment().parties().stream()
			.map(Party::name)
			.collect(Collectors.toList());
	}


	public class Part extends DivisorPlain
	{
		protected final List<Divisor> rowDivisors;
		public Part(DivisorOutput output, RoundingFunction r, String name, List<Divisor> rowDivisors)
		{
			super(output, r, name);
			this.rowDivisors = rowDivisors;
		}
		@Override public void partyColumn(StringTable.Column col, PlainOptions options)
		{ }
		@Override public void voteColumn(StringTable.Column col, PlainOptions options)
		{
			super.voteColumn(col, options);
			col.add(1, "");
			col.remove();
		}
		@Override public void resultColumn(StringTable.Column col, PlainOptions options)
		{
			super.resultColumn(col, options);
			if (options.divisorFormat() == DivisorFormat.QUOTIENTS)
			{
				col.add(1, col.get());
				col.set(divisor(output.divisor(), options));
			}
			else
			{
				col.add(1, col.get(col.size() - 2));
				col.remove(col.size() - 2);
			}
		}
		@Override public void quotientColumn(StringTable.Column col, PlainOptions options)
		{
			CollectionHelper.forEachPair(output.parties(), rowDivisors, (p, d) -> p.votes(p.votes().div(d.nice())));
			super.quotientColumn(col, options);
			col.add(1, "");
			col.remove();
		}
	}
}
