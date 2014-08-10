package de.uni_augsburg.bazi.bmm_pow;

import de.uni_augsburg.bazi.common.Resources;
import de.uni_augsburg.bazi.common.StringTable;
import de.uni_augsburg.bazi.common.plain.PlainOptions;
import de.uni_augsburg.bazi.common_vector.VectorPlain;
import de.uni_augsburg.bazi.divisor.DivisorAlgorithm;
import de.uni_augsburg.bazi.divisor.DivisorData;
import de.uni_augsburg.bazi.divisor.DivisorPlain;
import de.uni_augsburg.bazi.math.Real;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/** A PlainSupplier that generates plain data for the bmmp algorithm on request. */
public class BMMPowPlain
{
	protected final DivisorAlgorithm method;
	protected final BMMPowData data;
	protected final PlainOptions options;

	/**
	 * Constructor with initializers.
	 * @param data the result to produce plain data for.
	 * @param method the algorithm used for the actual apportionments.
	 * @param options
	 */
	public BMMPowPlain(DivisorAlgorithm method, BMMPowData data, PlainOptions options)
	{
		this.method = method;
		this.data = data;
		this.options = options;
	}


	public List<StringTable> get()
	{
		StringTable table = firstColumns();
		table.titles().add(data.name());

		AtomicInteger i = new AtomicInteger(1);
		data.results().forEach(
			r -> {
				PlainOptions opt = options.copy().cast(PlainOptions.class);
				opt.voteLabel(Resources.get("output.pop", r.power(), i.getAndIncrement()));
				StringTable t = method.plainFormatter().apply(r, opt).get(0);
				t.removeAll(VectorPlain.PARTY);
				t.cols(VectorPlain.VOTE).forEach(
					c ->
						c.subList(1, c.size()).replaceAll(
							s ->
								Real.valueOf(s).precision(1).toString()
						)
				);
				table.append(t);
			}
		);


		return Arrays.asList(table);
	}

	/**
	 * Returns the name and the vote column.
	 * @return the name and the vote column.
	 */
	public StringTable firstColumns()
	{
		StringTable table = new StringTable();
		DivisorPlain vp = new DivisorPlain(data.cast(DivisorData.class), options, method.roundingFunction(), "a");
		vp.partyColumn(table.col());
		vp.voteColumn(table.col());
		return table;
	}
}
