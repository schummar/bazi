package de.uni_augsburg.bazi.json;

import com.google.gson.*;
import de.schummar.castable.*;
import de.uni_augsburg.bazi.common.format.Format;

import java.io.PrintStream;
import java.io.Reader;

/** Serializes and deserializes data to and from json Strings. */
public class JsonFormat implements Format
{
	private final Gson gson = new GsonBuilder()
		.setPrettyPrinting()
		.registerTypeAdapter(CastableObject.class, OBJECT_SERIALIZER)
		.registerTypeAdapter(CastableList.class, LIST_SERIALIZER)
		.registerTypeAdapter(CastableString.class, STRING_SERIALIZER)
		.registerTypeAdapter(CastableUninitialized.class, UNINITIALIZED_SERIALIZER)
		.create();


	@Override public void configure(Data data) { }


	@Override public Castable deserialize(Reader reader)
	{
		JsonElement root = new JsonParser().parse(reader);
		return deserialize(root);
	}

	private CastableObject deserialize(JsonObject object)
	{
		CastableObject co = new CastableObject();
		object.entrySet().forEach(e -> co.put(e.getKey(), deserialize(e.getValue())));
		return co;
	}

	private CastableList deserialize(JsonArray array)
	{
		CastableList list = new CastableList();
		array.forEach(e -> list.add(deserialize(e)));
		return list;
	}

	private Castable deserialize(JsonElement element)
	{
		if (element.isJsonObject()) return deserialize(element.getAsJsonObject());
		if (element.isJsonArray()) return deserialize(element.getAsJsonArray());
		return new CastableString(element.getAsString());
	}


	@Override public void serialize(Castable data, PrintStream writer)
	{
		gson.toJson(data, writer);
	}

	private static final JsonSerializer<CastableObject> OBJECT_SERIALIZER =
		(src, typeOfSrc, context) -> src.isEmpty() ? JsonNull.INSTANCE : context.serialize(src.getValue());
	private static final JsonSerializer<CastableList> LIST_SERIALIZER =
		(src, typeOfSrc, context) -> src.isEmpty() ? JsonNull.INSTANCE : context.serialize(src.getValue());
	private static final JsonSerializer<CastableString> STRING_SERIALIZER =
		(src, typeOfSrc, context) -> src.getValue() == null ? JsonNull.INSTANCE : context.serialize(src.getValue());
	private static final JsonSerializer<CastableUninitialized> UNINITIALIZED_SERIALIZER =
		(src, typeOfSrc, context) -> JsonNull.INSTANCE;
}
