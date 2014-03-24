package de.uni_augsburg.bazi.common.plain;

import de.uni_augsburg.bazi.common.StringTable;

import java.util.List;

/** A PlainSupplier generates plain output on request. */
public interface PlainSupplier
{
	/**
	 * Generates plain output.
	 * @param options plain output options.
	 * @return One or several string tables that together make the plain output.
	 */
	List<StringTable> get(PlainOptions options);
}
