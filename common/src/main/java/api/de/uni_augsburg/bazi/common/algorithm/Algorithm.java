package de.uni_augsburg.bazi.common.algorithm;

import de.uni_augsburg.bazi.common.Plugin;
import de.uni_augsburg.bazi.common.PluginManager;
import de.uni_augsburg.bazi.common.data.Data;

import java.util.Collections;
import java.util.List;

/**
 * Basically a Data to Data function. Calling {@link #apply(Data, Options)}
 * automatically uses available Filters for pre- and postprocessing.
 */
public interface Algorithm<O extends Data> extends Plugin.Instance
{
	/** @return a list of Attributes this algorithm uses in its input data */
	List<Object> getInputAttributes();


	/**
	 * Applies this algorithm to the given input without using any filters.
	 * @param in input
	 * @param options general calculation options
	 * @return the algorithm's result
	 */
	O applyUnfiltered(Data in, Options options);


	/**
	 * Applies this algorithm to the given input. Automatically uses available Filters for pre- and postprocessing.
	 * @param in input
	 * @param options general calculation options
	 * @return the algorithm's result
	 */
	default O apply(Data in, Options options)
	{
		List<Filter> filters = PluginManager.INSTANCE.getFiltersFor(this);
		filters.forEach(f -> f.preprocess(in));

		O out = applyUnfiltered(in, options);

		Collections.reverse(filters);
		filters.forEach(f -> f.postprocess(in, out));

		return out;
	}
}
