package de.uni_augsburg.bazi.json;

import de.uni_augsburg.bazi.common.format.FormatPlugin;

public class JsonPlugin implements FormatPlugin
{
	@Override public Json createFileFormat(String name) { return name.equals("json") ? new Json() : null; }
}
