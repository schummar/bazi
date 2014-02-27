package de.uni_augsburg.bazi.json;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import de.uni_augsburg.bazi.common.Resources;
import de.uni_augsburg.bazi.common.format.Converters;
import de.uni_augsburg.bazi.common.format.Format;
import de.uni_augsburg.bazi.common.util.MList;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Marco on 21.02.14.
 */
public class JsonFormat implements Format
{
	private final Gson gson = new GsonBuilder()
		.setPrettyPrinting()
		.registerTypeAdapterFactory(TYPE_ADAPTER_FACTORY)
		.create();


	@Override public Map<String, Object> deserialize(String s)
	{
		JsonElement root = new JsonParser().parse(s);
		if (!root.isJsonObject()) throw new RuntimeException(Resources.get("format.invalid"));
		return deserialize(root.getAsJsonObject());
	}

	private Map<String, Object> deserialize(JsonObject object)
	{
		Map<String, Object> map = new HashMap<>();
		for (Map.Entry<String, JsonElement> e : object.entrySet())
			map.put(e.getKey(), deserialize(e.getValue()));
		return map;
	}

	private MList<Object> deserialize(JsonArray array)
	{
		MList<Object> list = new MList<>();
		for (JsonElement element : array)
			list.add(deserialize(element));
		return list;
	}

	private Object deserialize(JsonElement element)
	{
		if (element.isJsonObject()) return deserialize(element.getAsJsonObject());
		if (element.isJsonArray()) return deserialize(element.getAsJsonArray());
		return element.getAsString();
	}


	@Override public String serialize(Map<String, Object> map)
	{
		return gson.toJson(map);
	}

	private static final TypeAdapterFactory TYPE_ADAPTER_FACTORY = new TypeAdapterFactory()
	{
		@Override public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type)
		{
			TypeAdapterFactory that = this;
			return new TypeAdapter<T>()
			{
				@Override public void write(JsonWriter out, T value) throws IOException
				{
					Object converted = Converters.serialize(value);
					if (converted == value)
						delegateHelper(gson, that, converted.getClass(), out, converted);
					else
						gson.toJson(Converters.serialize(value), value.getClass(), out);
				}
				@Override public T read(JsonReader in) throws IOException
				{
					return null;
				}
			}.nullSafe();
		}
	};

	private static <T> void delegateHelper(Gson gson, TypeAdapterFactory factory, Class<T> type, JsonWriter out, Object instance) throws IOException
	{
		gson.getDelegateAdapter(factory, TypeToken.get(type)).write(out, type.cast(instance));
	}
}
