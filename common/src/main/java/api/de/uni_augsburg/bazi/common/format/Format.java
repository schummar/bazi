package de.uni_augsburg.bazi.common.format;

import de.uni_augsburg.bazi.common.PluginManager;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

/**
 * Created by Marco on 21.02.14.
 */
public interface Format
{
	public static Format create(String name, PluginManager manager)
	{
		return manager.create(FormatPlugin.class, p -> p.createFileFormat(name));
	}


	public Map<String, Object> deserialize(InputStream stream);
	public void serialize(Map<String, ? extends Object> map, OutputStream stream);
}
