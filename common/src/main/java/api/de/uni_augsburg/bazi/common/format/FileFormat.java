package de.uni_augsburg.bazi.common.format;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

/**
* Created by Marco on 21.02.14.
*/
public interface FileFormat
{
	public Map<String, Object> deserialize(InputStream stream);
	public void serialize(Map<String, ? extends Object> map, OutputStream stream);
}
