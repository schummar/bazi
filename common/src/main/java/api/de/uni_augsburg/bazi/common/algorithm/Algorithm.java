package de.uni_augsburg.bazi.common.algorithm;

import de.uni_augsburg.bazi.common.Plugin;
import de.uni_augsburg.bazi.common.PluginManager;
import de.uni_augsburg.bazi.common.data.Data;

import java.util.Collections;
import java.util.List;

public interface Algorithm<O extends Data> extends Plugin.Instance
{
	List<Object> getInputAttributes();

	O applyUnfiltered(Data in, Options options);

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
