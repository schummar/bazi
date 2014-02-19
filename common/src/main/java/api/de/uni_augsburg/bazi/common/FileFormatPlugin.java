package de.uni_augsburg.bazi.common;

import java.util.Map;

public interface FileFormatPlugin
{
	public FileFormat getFileFormat();


	public interface FileFormat
	{
		public Map<String, Object> deserialize(String string);
		public String serialized(Map<String, ? extends Object> map);
	}
}
