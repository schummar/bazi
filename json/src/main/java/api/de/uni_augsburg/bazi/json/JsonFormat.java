package de.uni_augsburg.bazi.json;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import de.uni_augsburg.bazi.common.Resources;
import de.uni_augsburg.bazi.common.data.Data;
import de.uni_augsburg.bazi.common.format.Converters;
import de.uni_augsburg.bazi.common.format.Format;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
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
		Map<String, Object> map = new LinkedHashMap<>();
		for (Map.Entry<String, JsonElement> e : object.entrySet())
			map.put(e.getKey(), deserialize(e.getValue()));
		return map;
	}

	private List<Object> deserialize(JsonArray array)
	{
		List<Object> list = new ArrayList<>();
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


	@Override public String serialize(Data data)
	{
		return gson.toJson(data.toMap());
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
						gson.toJson(converted, converted.getClass(), out);
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
