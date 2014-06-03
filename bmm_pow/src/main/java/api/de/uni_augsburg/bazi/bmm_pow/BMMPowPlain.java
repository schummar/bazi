package de.uni_augsburg.bazi.bmm_pow;

import de.uni_augsburg.bazi.common.Resources;
import de.uni_augsburg.bazi.common.StringTable;
import de.uni_augsburg.bazi.common.plain.PlainOptions;
import de.uni_augsburg.bazi.common.plain.PlainSupplier;
import de.uni_augsburg.bazi.common_vector.VectorPlain;
import de.uni_augsburg.bazi.divisor.DivisorAlgorithm;
import de.uni_augsburg.bazi.divisor.DivisorData;
import de.uni_augsburg.bazi.divisor.DivisorPlain;
import de.uni_augsburg.bazi.math.Real;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/** A PlainSupplier that generates plain output for the bmmp algorithm on request. */
public class BMMPowPlain implements PlainSupplier
{
	protected final BMMPowOutput output;
	protected final DivisorAlgorithm method;

	/**
	 * Constructor with initializers.
	 * @param output the result to produce plain output for.
	 * @param method the algorithm used for the actual apportionments.
	 */
	public BMMPowPlain(BMMPowOutput output, DivisorAlgorithm method)
	{
		this.output = output;
		this.method = method;
	}


	@Override public List<StringTable> get(PlainOptions options)
	{
		List<StringTable> partTables = new ArrayList<>();
		output.results().forEach(r -> partTables.addAll(r.plain().get(options)));

		StringTable table = firstColumns(options);

		AtomicInteger i = new AtomicInteger(1);
		output.results().forEach(
			r -> {
				PlainOptions opt = options.copy(PlainOptions.class);
				opt.voteLabel(Resources.get("output.pop", r.power(), i.getAndIncrement()));
				StringTable t = r.plain().get(opt).get(0);
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
	 * @param options output options.
	 * @return the name and the vote column.
	 */
	public StringTable firstColumns(PlainOptions options)
	{
		StringTable table = new StringTable();
		DivisorPlain vp = new DivisorPlain(output.cast(DivisorData.class), method.roundingFunction(), "a");
		vp.partyColumn(table.col(), options);
		vp.voteColumn(table.col(), options);
		return table;
	}
}
