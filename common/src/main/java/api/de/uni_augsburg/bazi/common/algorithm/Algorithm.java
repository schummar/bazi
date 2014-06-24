package de.uni_augsburg.bazi.common.algorithm;

import de.schummar.castable.Data;
import de.uni_augsburg.bazi.common.Plugin;
import de.uni_augsburg.bazi.common.PluginManager;
import de.uni_augsburg.bazi.common.StringTable;
import de.uni_augsburg.bazi.common.plain.PlainOptions;

import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;

/** An algorithm that in some way calculates seat apportionments out of votes and other input data. */
public interface Algorithm extends Plugin.Instance
{
	/**
	 * The display name of this algorithm.
	 * @return the display name of this algorithm.
	 */
	String name();

	BiFunction<Data,PlainOptions,List<StringTable>> plainFormatter();

	/**
	 * Applies this algorithm to the given input without using any filters.
	 * @param options general calculation options
	 */
	void applyUnfiltered(Data data, Options options);

	/**
	 * Applies this algorithm to the given input. Automatically uses available Filters for pre- and postprocessing.
	 * @param data input
	 * @param options general calculation options
	 */
	default void apply(Data data, Options options)
	{
		List<Filter> filters = PluginManager.getFiltersFor(this);
		filters.forEach(f -> f.preprocess(data));

		applyUnfiltered(data, options);

		Collections.reverse(filters);
		filters.forEach(f -> f.postprocess(data));
	}
}
