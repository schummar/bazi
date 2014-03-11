package de.uni_augsburg.bazi.biprop;

import de.uni_augsburg.bazi.common.StringTable;
import de.uni_augsburg.bazi.common.plain.PlainOptions;
import de.uni_augsburg.bazi.common.plain.PlainSupplier;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marco on 11.03.14.
 */
public class BipropPlain implements PlainSupplier
{
	private BipropOutput output;
	public BipropPlain(BipropOutput output)
	{
		this.output = output;
	}

	@Override public List<StringTable> get(PlainOptions options)
	{
		List<StringTable> tables = new ArrayList<>();
		tables.addAll(output.superApportionment().plain().get(options));

		StringTable table = new StringTable();
		table.col().add("...");
		tables.add(table);
		return tables;
	}
}
