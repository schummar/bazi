package de.uni_augsburg.bazi.common.format;

import de.uni_augsburg.bazi.common.Plugin;

import java.util.Map;

/**
 * Created by Marco on 21.02.14.
 */
public interface Format extends Plugin.Instance
{
	Map<String, Object> deserialize(String s);
	String serialize(Map<String, Object> map);
}
