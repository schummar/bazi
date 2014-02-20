package de.uni_augsburg.bazi.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import de.uni_augsburg.bazi.common.FileFormatPlugin;

import java.io.*;
import java.util.Map;

public class JsonPlugin implements FileFormatPlugin
{
	@Override public Json createFileFormat(String name) { return name.equals("json") ? new Json() : null; }

	public static class Json implements FileFormat
	{
		private final Gson gson = new GsonBuilder()
			.setPrettyPrinting()
			.create();

		@Override public Map<String, Object> deserialize(InputStream stream)
		{
			try (InputStreamReader reader = new InputStreamReader(stream, "UTF-8"))
			{
				return gson.fromJson(reader, new TypeToken<Map<String, Object>>() {}.getType());
			}
			catch (IOException e)
			{
				throw new RuntimeException(e);
			}
		}

		@Override public void serialize(Map<String, ? extends Object> map, OutputStream stream)
		{
			try (OutputStreamWriter writer = new OutputStreamWriter(stream, "UTF-8"))
			{
				gson.toJson(map, writer);
			}
			catch (IOException e)
			{
				throw new RuntimeException(e);
			}

		}
	}
}
