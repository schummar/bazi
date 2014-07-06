package de.uni_augsburg.bazi.quota;


import de.schummar.castable.Data;
import de.uni_augsburg.bazi.common.StringTable;
import de.uni_augsburg.bazi.common.algorithm.Algorithm;
import de.uni_augsburg.bazi.common.algorithm.Options;
import de.uni_augsburg.bazi.common.plain.PlainOptions;

import java.util.List;
import java.util.function.BiFunction;

/** The quota algorithm. */
public class QuotaAlgorithm implements Algorithm<QuotaData>
{
	private final QuotaFunction quotaFunction;
	private final ResidualHandler residualHandler;
	private final String name;

	/**
	 * @param quotaFunction the quota function.
	 * @param residualHandler the residual handler.
	 * @param name the display name of this algorithm.
	 */
	public QuotaAlgorithm(QuotaFunction quotaFunction, ResidualHandler residualHandler, String name)
	{
		this.quotaFunction = quotaFunction;
		this.residualHandler = residualHandler;
		this.name = name;
	}

	@Override public String name() { return name; }
	@Override public Class<QuotaData> dataType() { return QuotaData.class; }

	@Override public BiFunction<Data, PlainOptions, List<StringTable>> plainFormatter()
	{
		return (data, options) -> new QuotaPlain(data.cast(QuotaData.class), options, name).get();
	}

	@Override public void apply(Data data, Options options)
	{
		QuotaAlgorithmImpl.calculate(
			data.cast(QuotaData.class),
			quotaFunction,
			residualHandler,
			name
		);
	}
}
