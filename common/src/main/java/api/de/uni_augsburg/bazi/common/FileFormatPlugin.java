package de.uni_augsburg.bazi.common;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

public interface FileFormatPlugin
{
	public FileFormat createFileFormat(String name);


	public interface FileFormat
	{
		public Map<String, Object> deserialize(InputStream stream);
		public void serialize(Map<String, ? extends Object> map, OutputStream stream);
	}
}
