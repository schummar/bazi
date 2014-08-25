package de.uni_augsburg.bazi.list;

import de.uni_augsburg.bazi.common.Resources;
import de.uni_augsburg.bazi.common.StringTable;
import de.uni_augsburg.bazi.common.algorithm.Algorithm;
import de.uni_augsburg.bazi.common.algorithm.VectorData;
import de.uni_augsburg.bazi.common.plain.PlainOptions;

import java.util.ArrayList;
import java.util.List;

/** A PlainSupplier that generates plain data for the ListAlgorithm on request. */
public class ListPlain
{
	private final ListData data;
	private final PlainOptions options;
	private final Algorithm<? extends VectorData> Super, sub;

	/**
	 * @param data the data to produce plain data for.
	 * @param options options.
	 * @param Super the super method.
	 * @param sub the sub method.
	 */
	public ListPlain(ListData data, PlainOptions options, Algorithm<? extends VectorData> Super, Algorithm<? extends VectorData> sub)
	{
		this.data = data;
		this.options = options;
		this.Super = Super;
		this.sub = sub;
	}


	public List<StringTable> get()
	{
		List<StringTable> tables = new ArrayList<>();
		tables.addAll(Super.plainFormatter().apply(data, options));
		data.parties().forEach(
			p -> {
				if (p.parties() != null && p.parties().size() > 0)
				{
					tables.addAll(sub.plainFormatter().apply(p, options));
					StringTable table = tables.get(tables.size() - 1);
					String title = table.titles().get(0);
					table.titles().set(0, Resources.get("output.subapportionment", title));
				}
			}
		);
		return tables;
	}
}
