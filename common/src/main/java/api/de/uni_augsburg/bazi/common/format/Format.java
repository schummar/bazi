package de.uni_augsburg.bazi.common.format;

import de.uni_augsburg.bazi.common.Plugin;
import de.uni_augsburg.bazi.common.data.Data;

import java.util.Map;

/** A format plugin is used to convert file formats (json,xml,...) to raw data and vice versa. */
public interface Format extends Plugin.Instance
{
	/**
	 * Deserialize the given String to a map of raw data.
	 * Each entry represents an attribute.
	 * The key is the attribute name.
	 * The value must either be a String, a List of values or again an attribute map.
	 * @param s the serialized data.
	 * @return a raw data map.
	 */
	Map<String, Object> deserialize(String s);

	/**
	 * Serialize the given map of raw data.
	 * Each entry represents an attribute.
	 * The key is the attribute name.
	 * The value must either be a String, a List of values or again an attribute map.
	 * @param data a raw data map.
	 * @return the serialized data.
	 */
	String serialize(Data data);

	/**
	 * Configure the format.
	 * Usually plugins are configured on creation. But since input files may contain format
	 * parameters the configuration can be updated.
	 * @param data the configuration data.
	 */
	void configure(Data data);
}
