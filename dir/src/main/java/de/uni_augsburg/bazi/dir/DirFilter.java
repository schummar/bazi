package de.uni_augsburg.bazi.dir;

import de.schummar.castable.Data;
import de.uni_augsburg.bazi.common.StringTable;
import de.uni_augsburg.bazi.common.algorithm.Algorithm;
import de.uni_augsburg.bazi.common.algorithm.Filter;
import de.uni_augsburg.bazi.common.algorithm.Options;
import de.uni_augsburg.bazi.common.algorithm.VectorData;
import de.uni_augsburg.bazi.common.plain.PlainOptions;

import java.util.List;
import java.util.function.BiFunction;

import static de.uni_augsburg.bazi.dir.DirData.Party;

/** The direct seats filter. */
public class DirFilter implements Filter
{
	@Override public <T extends Data> Algorithm<T> decorate(Algorithm<T> algorithm)
	{
		if (!VectorData.class.isAssignableFrom(algorithm.dataType())) return algorithm;

		@SuppressWarnings("unchecked")
		Algorithm<? extends VectorData> vAlgorithm = (Algorithm<? extends VectorData>) algorithm;

		return new Algorithm<T>()
		{
			@Override public String name()
			{
				return algorithm.name();
			}
			@Override public Class<T> dataType()
			{
				return algorithm.dataType();
			}
			@Override public Algorithm<T> unwrap()
			{
				return algorithm.unwrap();
			}
			@Override public BiFunction<Data, PlainOptions, List<StringTable>> plainFormatter()
			{
				return (data, options) -> new DirPlain(data.cast(DirData.class), options, vAlgorithm).get();
			}
			@Override public void apply(Data data, Options options)
			{
				algorithm.apply(data, options);
				DirData dirData = data.cast(DirData.class);
				dirData.parties().forEach(DirFilter::sumDir);
			}
		};
	}

	private static void sumDir(Party party)
	{
		party.parties().forEach(
			p -> {
				Party dp = p.cast(DirData.Party.class);
				sumDir(dp);
				party.dir(party.dir().add(dp.dir()));
			}
		);
	}
}
