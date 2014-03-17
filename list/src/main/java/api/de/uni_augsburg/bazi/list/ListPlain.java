package de.uni_augsburg.bazi.list;

import de.uni_augsburg.bazi.common.Resources;
import de.uni_augsburg.bazi.common.StringTable;
import de.uni_augsburg.bazi.common.plain.PlainOptions;
import de.uni_augsburg.bazi.common.plain.PlainSupplier;

import java.util.ArrayList;
import java.util.List;

public class ListPlain implements PlainSupplier
{
	private final ListOutput output;
	private final PlainSupplier mainSupplier;
	public ListPlain(ListOutput output)
	{
		this.output = output;
		this.mainSupplier = output.plain();
	}


	@Override public List<StringTable> get(PlainOptions options)
	{
		List<StringTable> tables = new ArrayList<>();
		tables.addAll(mainSupplier.get(options));
		output.parties().forEach(
			p -> {
				if (p.parties() != null && p.parties().size() > 0)
				{
					tables.addAll(p.plain().get(options));
					StringTable table = tables.get(tables.size() - 1);
					String title = table.titles().get(0);
					table.titles().set(0, Resources.get("output.subapportionment", title));
				}
			}
		);
		return tables;
	}
}
