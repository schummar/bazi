package de.uni_augsburg.bazi.common.format;

import de.uni_augsburg.bazi.common.PluginManager;

public interface FormatPlugin extends PluginManager.Plugin
{
	public Format createFileFormat(String name);
}
