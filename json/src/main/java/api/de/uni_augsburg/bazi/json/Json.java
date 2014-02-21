package de.uni_augsburg.bazi.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import de.uni_augsburg.bazi.common.format.Format;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Created by Marco on 21.02.14.
 */
public class Json implements Format
{
	private final Gson gson = new GsonBuilder()
		.setPrettyPrinting()
		.create();

	@Override public Map<String, Object> deserialize(InputStream stream)
	{
		try (InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8))
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
		try (OutputStreamWriter writer = new OutputStreamWriter(stream, StandardCharsets.UTF_8))
		{
			gson.toJson(map, writer);
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}

	}
}
