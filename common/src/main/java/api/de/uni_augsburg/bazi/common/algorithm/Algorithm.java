package de.uni_augsburg.bazi.common.algorithm;

import de.schummar.castable.CastableObject;
import de.schummar.castable.Data;
import de.uni_augsburg.bazi.common.Plugin;
import de.uni_augsburg.bazi.common.PluginManager;

import java.util.Collections;
import java.util.List;

/** An algorithm that in some way calculates seat apportionments out of votes and other input data. */
public interface Algorithm extends Plugin.Instance
{
	/**
	 * The display name of this algorithm.
	 * @return the display name of this algorithm.
	 */
	String name();

	/**
	 * The list of Attributes this algorithm uses in its input data.
	 * Since the inputs are generic this method gives information about attributes that can or must be included and their types.
	 * @return the list of Attributes this algorithm uses in its input data.
	 */
	List<Object> getInputAttributes();

	/**
	 * Applies this algorithm to the given input without using any filters.
	 * @param in input
	 * @param options general calculation options
	 * @return the algorithm's result
	 */
	void applyUnfiltered(Data data, Options options);

	/**
	 * Applies this algorithm to the given input. Automatically uses available Filters for pre- and postprocessing.
	 * @param data input
	 * @param options general calculation options
	 * @return the algorithm's result
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
