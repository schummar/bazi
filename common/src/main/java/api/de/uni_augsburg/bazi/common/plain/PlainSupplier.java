package de.uni_augsburg.bazi.common.plain;

import de.uni_augsburg.bazi.common.StringTable;

import java.util.List;

public interface PlainSupplier
{
	List<StringTable> get(PlainOptions options);
}
