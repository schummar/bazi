package de.uni_augsburg.bazi.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import de.uni_augsburg.bazi.common.format.Format;

import java.util.Map;

/**
 * Created by Marco on 21.02.14.
 */
public class JsonFormat implements Format
{
	private final Gson gson = new GsonBuilder()
		.setPrettyPrinting()
		.create();

	@Override public Map<String, Object> deserialize(String s)
	{
		return gson.fromJson(s, new TypeToken<Map<String, Object>>() {}.getType());
	}

	@Override public String serialize(Map<String, ? extends Object> map)
	{
		return gson.toJson(map);
	}
}
