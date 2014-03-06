package de.uni_augsburg.bazi.common.plain;

import de.uni_augsburg.bazi.common.StringTable;

import java.util.List;

/**
 * Created by Marco on 06.03.14.
 */
public interface PlainSupplier
{
	List<StringTable> get(PlainOptions options);
}
