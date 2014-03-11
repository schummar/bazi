package de.uni_augsburg.bazi.biprop;

import de.uni_augsburg.bazi.common.Resources;
import de.uni_augsburg.bazi.common.StringTable;
import de.uni_augsburg.bazi.common.data.Data;
import de.uni_augsburg.bazi.common.data.Default;
import de.uni_augsburg.bazi.common.plain.PlainOptions;
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

import static de.uni_augsburg.bazi.common.algorithm.VectorOutput.Party;

/**
 * Created by Marco on 11.03.14.
 */
public class BipropPlain extends MatrixPlain
{
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
		firstColumn(table.col(), options);
		table.append(getParts(options));
		voteSumColumn(table.col(), options);
		seatSumColumn(table.col(), options);
		return table;
	}


	@Override public StringTable getPart(Object key, PlainOptions options)
	{
		StringTable table = new StringTable();

		DivisorOutput out = Data.create(DivisorOutput.class);
		out.parties(parties(key));
		out.divisor(new Divisor(BMath.ONE, BMath.ONE));
		PlainOptions opt = options.copy(PlainOptions.class);
		opt.voteLabel(label(key));
		StringTable part = new DivisorPlain(out, RoundingFunction.DIV_STD, vectorName).get(opt).get(0);
		part.col(0).delete();
		part.cols().forEach(c -> c.add(1, ""));
		table.append(part);

		return table;
	}

	public Divisor divisor(Object key)
	{
		return null;
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


	public interface BipropPlainOptions extends PlainOptions
	{
		@Default("true") boolean sortSuper();
	}
}
