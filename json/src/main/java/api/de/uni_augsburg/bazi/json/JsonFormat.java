package de.uni_augsburg.bazi.json;

import com.google.gson.*;
import de.schummar.castable.*;
import de.uni_augsburg.bazi.common.format.Format;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Map;

/** Serializes and deserializes data to and from json Strings. */
public class JsonFormat implements Format
{
	private final Gson gson = new GsonBuilder()
		.setPrettyPrinting()
		.registerTypeAdapter(
			CastableString.class, (JsonSerializer<CastableString>) (src, typeOfSrc, context) -> new JsonPrimitive(src.getValue())
		)
		.create();


	@Override public void configure(Data data) { }


	@Override public Castable deserialize(InputStream stream)
	{
		JsonElement root = new JsonParser().parse(new InputStreamReader(stream));
		return deserialize(root);
	}

	private CastableObject deserialize(JsonObject object)
	{
		CastableObject co = new CastableObject();
		for (Map.Entry<String, JsonElement> e : object.entrySet())
			co.put(e.getKey(), deserialize(e.getValue()));
		return co;
	}

	private CastableList deserialize(JsonArray array)
	{
		CastableList list = new CastableList();
		for (JsonElement element : array)
			list.add(deserialize(element));
		return list;
	}

	private Castable deserialize(JsonElement element)
	{
		if (element.isJsonObject()) return deserialize(element.getAsJsonObject());
		if (element.isJsonArray()) return deserialize(element.getAsJsonArray());
		return new CastableString(element.getAsString());
	}


	@Override public void serialize(Castable data, OutputStream stream)
	{
		gson.toJson(data, new OutputStreamWriter(stream));
	}
}
