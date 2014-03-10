package de.uni_augsburg.bazi.common.algorithm;

import de.uni_augsburg.bazi.common.Plugin;
import de.uni_augsburg.bazi.common.PluginManager;
import de.uni_augsburg.bazi.common.data.Data;

import java.util.Collections;
import java.util.List;

/**
 * Created by Marco on 21.02.14.
 */
public abstract class Algorithm<O extends Data> implements Plugin.Instance
{
	public abstract List<Object> getInputAttributes();

	public abstract O applyUnfiltered(Data in);

	public final O apply(Data in)
	{
		List<Filter> filters = PluginManager.INSTANCE.getFiltersFor(this);
		filters.forEach(f -> f.preprocess(in));

		O out = applyUnfiltered(in);

		Collections.reverse(filters);
		filters.forEach(f -> f.postprocess(in, out));

		return out;
	}
}
