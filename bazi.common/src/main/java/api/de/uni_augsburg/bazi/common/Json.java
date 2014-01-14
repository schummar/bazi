package de.uni_augsburg.bazi.common;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class Json
{
	public static Gson createGson()
	{
		return new GsonBuilder()
				.setPrettyPrinting()
				.registerTypeAdapterFactory(TYPE_ADAPTER_FACTORY)
				.create();
	}

	private static final TypeAdapterFactory TYPE_ADAPTER_FACTORY = new TypeAdapterFactory()
	{
		private final Map<TypeToken<?>, TypeAdapter<?>> adapters = new HashMap<>();

		@SuppressWarnings("unchecked") @Override public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type)
		{
			if (adapters.containsKey(type))
				return (TypeAdapter<T>) adapters.get(type);

			boolean serializeAsString = isSerializeAsString(type);
			Class<? extends T> deserializeAsClass = getDeserializeAsClass(type);
			TypeAdapter<? extends T> adapterToDeserialize = deserializeAsClass != null ? gson.getAdapter(deserializeAsClass) : null;
			Serializer<T> serializer = getSerializer(type);
			Deserializer<T> deserializer = getDeserializer(type);
			TypeAdapter<T> def = null;
			try
			{
				def = gson.getDelegateAdapter(TYPE_ADAPTER_FACTORY, type);
			}
			catch (Exception e)
			{}
			TypeAdapter<T> ta = new Adapter<>(serializeAsString, adapterToDeserialize, serializer, deserializer, def).nullSafe();

			adapters.put(type, ta);
			return ta;
		}
	};

	private static class Adapter<T> extends TypeAdapter<T>
	{
		private final boolean serializeAsString;
		private final TypeAdapter<? extends T> adapterToDeserialize;
		private final Serializer<T> serializer;
		private final Deserializer<T> deserializer;
		private final TypeAdapter<T> def;

		public Adapter(boolean serializeAsString, TypeAdapter<? extends T> adapterToDeserialize, Serializer<T> serializer, Deserializer<T> deserializer, TypeAdapter<T> def)
		{
			this.serializeAsString = serializeAsString;
			this.adapterToDeserialize = adapterToDeserialize;
			this.serializer = serializer;
			this.deserializer = deserializer;
			this.def = def;
		}
		@Override public void write(JsonWriter out, T value) throws IOException
		{
			if (serializeAsString)
				out.value(value.toString());
			else if (serializer != null)
				serializer.serialize(out, value);
			else
				def.write(out, value);
		}
		@Override public T read(JsonReader in) throws IOException
		{
			if (adapterToDeserialize != null)
				return adapterToDeserialize.read(in);
			if (deserializer != null)
				return deserializer.deserialize(in);
			return def.read(in);
		}
	}


	// //////////////////////////////////////////////////////////////////////////

	@Retention(RetentionPolicy.RUNTIME) @Target(ElementType.TYPE) public static @interface SerializeAsString
	{}

	@Retention(RetentionPolicy.RUNTIME) @Target(ElementType.TYPE) public static @interface DeserializeFromString
	{}

	@Retention(RetentionPolicy.RUNTIME) @Target(ElementType.TYPE) public static @interface DeserializeAsClass
	{
		Class<?> value();
	}

	@Retention(RetentionPolicy.RUNTIME) @Target(ElementType.TYPE) public static @interface Serialize
	{
		Class<? extends Serializer<?>> value();
	}

	public static interface Serializer<T>
	{
		public void serialize(JsonWriter out, T value) throws IOException;
	}

	@Retention(RetentionPolicy.RUNTIME) @Target(ElementType.TYPE) public static @interface Deserialize
	{
		Class<? extends Deserializer<?>> value();
	}

	public static interface Deserializer<T>
	{
		public T deserialize(JsonReader in) throws IOException;
	}

	public static <T> boolean isSerializeAsString(TypeToken<T> type)
	{
		return type.getRawType().isAnnotationPresent(SerializeAsString.class);
	}

	@SuppressWarnings("unchecked") public static <T> Class<? extends T> getDeserializeAsClass(TypeToken<T> type)
	{
		DeserializeAsClass s = type.getRawType().getAnnotation(DeserializeAsClass.class);
		if (s == null)
			return null;

		return (Class<? extends T>) s.value();
	}

	@SuppressWarnings("unchecked") public static <T> Serializer<T> getSerializer(TypeToken<T> type)
	{
		Serialize s = type.getRawType().getAnnotation(Serialize.class);
		if (s == null)
			return null;

		try
		{
			return (Serializer<T>) s.value().newInstance();
		}
		catch (InstantiationException | IllegalAccessException e)
		{}
		return null;
	}

	@SuppressWarnings("unchecked") public static <T> Deserializer<T> getDeserializer(TypeToken<T> type)
	{
		if (type.getRawType().isAnnotationPresent(SerializeAsString.class))
		{
			try
			{
				Method m = type.getRawType().getMethod("valueOf", String.class);
				return json -> {
					try
					{
						return (T) m.invoke(null, json.nextString());
					}
					catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException | IOException e)
					{}
					return null;
				};
			}
			catch (NoSuchMethodException | SecurityException e)
			{
				e.printStackTrace();
			}
		}


		Deserialize s = type.getRawType().getAnnotation(Deserialize.class);
		if (s == null)
			return null;

		try
		{
			return (Deserializer<T>) s.value().newInstance();
		}
		catch (InstantiationException | IllegalAccessException e)
		{}
		return null;
	}

	// //////////////////////////////////////////////////////////////////////////


	public static <T> ImmutableList<String> checkJson(String json, Class<T> type)
	{
		return checkJson(json, TypeToken.get(type));
	}

	public static <T> ImmutableList<String> checkJson(String json, TypeToken<T> type)
	{
		JsonReader reader = new JsonReader(new StringReader(json));
		reader.setLenient(false);
		JsonElement root = new JsonParser().parse(reader);
		return checkJson(root, type);
	}

	public static <T> ImmutableList<String> checkJson(JsonElement element, TypeToken<T> type)
	{
		List<Pair<JsonElement, ? extends TypeToken<?>>> queue = new ArrayList<>();
		queue.add(Pair.of(element, type));

		List<String> warnings = new ArrayList<>();

		while (!queue.isEmpty())
		{
			Pair<JsonElement, ? extends TypeToken<?>> pair = queue.remove(0);
			JsonElement currentElement = pair.getFirst();
			TypeToken<?> currentType = pair.getSecond();

			if (currentElement.isJsonNull() || currentElement.isJsonPrimitive())
			{
				try (JsonReader reader = new JsonReader(new StringReader(currentElement.getAsString())))
				{
					reader.setLenient(true);
					if (createGson().getAdapter(currentType).fromJsonTree(currentElement) != null)
						continue;
				}
				catch (Exception e)
				{
					warnings.add(Resources.get("json.primitive_to_object", currentType.getType(), currentElement.getAsString()));
				}
			}

			else if (currentElement.isJsonArray())
			{
				JsonArray ja = currentElement.getAsJsonArray();

				Type childType = null;
				if (currentType.getRawType().isArray())
					childType = $Gson$Types.getArrayComponentType(currentType.getType());
				else if (Collection.class.isAssignableFrom(currentType.getRawType()))
					childType = $Gson$Types.getCollectionElementType(currentType.getType(), currentType.getRawType());

				if (childType == null)
				{
					try (JsonReader reader = new JsonReader(new StringReader(ja.toString())))
					{
						reader.setLenient(true);
						if (createGson().getAdapter(currentType).fromJsonTree(currentElement) != null)
							continue;
					}
					catch (Exception e)
					{
						warnings.add(Resources.get("json.array_to_object", currentType.getType(), ja.toString()));
					}
				}
				else
				{
					for (JsonElement entry : ja)
						queue.add(Pair.of(entry, TypeToken.get(childType)));
				}
			}

			else if (currentElement.isJsonObject())
			{
				if (getDeserializer(currentType) != null)
					continue;

				Class<?> currentClass = getDeserializeAsClass(currentType);
				if (currentClass == null)
					currentClass = currentType.getRawType();

				JsonObject jo = currentElement.getAsJsonObject();
				for (Entry<String, JsonElement> entry : jo.entrySet())
				{
					String key = entry.getKey();
					boolean found = false;
					for (Field field : currentClass.getFields())
						if (field.getName().equals(key))
						{
							found = true;
							queue.add(Pair.of(entry.getValue(), TypeToken.get(field.getGenericType())));
							break;
						}
					if (!found)
						warnings.add(Resources.get("json.unknown_key", key, currentType.getType()));
				}
			}
		}

		return ImmutableList.copyOf(warnings);
	}


	// Gson delegate ////////////////////////////////////////////////////////////


	private static Gson DEFAULT = null;

	public static Gson getDefault()
	{
		if (DEFAULT == null)
			DEFAULT = createGson();
		return DEFAULT;
	}

	public static <T> TypeAdapter<T> getAdapter(TypeToken<T> type)
	{
		return getDefault().getAdapter(type);
	}

	public static <T> TypeAdapter<T> getDelegateAdapter(TypeAdapterFactory skipPast, TypeToken<T> type)
	{
		return getDefault().getDelegateAdapter(skipPast, type);
	}

	public static <T> TypeAdapter<T> getAdapter(Class<T> type)
	{
		return getDefault().getAdapter(type);
	}

	public static JsonElement toJsonTree(Object src)
	{
		return getDefault().toJsonTree(src);
	}

	public static JsonElement toJsonTree(Object src, Type typeOfSrc)
	{
		return getDefault().toJsonTree(src, typeOfSrc);
	}

	public static String toJson(Object src)
	{
		return getDefault().toJson(src);
	}

	public static String toJson(Object src, Type typeOfSrc)
	{
		return getDefault().toJson(src, typeOfSrc);
	}

	public static void toJson(Object src, Appendable writer) throws JsonIOException
	{
		getDefault().toJson(src, writer);
	}

	public static void toJson(Object src, Type typeOfSrc, Appendable writer) throws JsonIOException
	{
		getDefault().toJson(src, typeOfSrc, writer);
	}

	public static void toJson(Object src, Type typeOfSrc, JsonWriter writer) throws JsonIOException
	{
		getDefault().toJson(src, typeOfSrc, writer);
	}

	public static String toJson(JsonElement jsonElement)
	{
		return getDefault().toJson(jsonElement);
	}

	public static void toJson(JsonElement jsonElement, Appendable writer) throws JsonIOException
	{
		getDefault().toJson(jsonElement, writer);
	}

	public static void toJson(JsonElement jsonElement, JsonWriter writer) throws JsonIOException
	{
		getDefault().toJson(jsonElement, writer);
	}

	public static <T> T fromJson(String json, Class<T> classOfT) throws JsonSyntaxException
	{
		return getDefault().fromJson(json, classOfT);
	}

	public static <T> T fromJson(String json, Type typeOfT) throws JsonSyntaxException
	{
		return getDefault().fromJson(json, typeOfT);
	}

	public static <T> T fromJson(Reader json, Class<T> classOfT) throws JsonSyntaxException, JsonIOException
	{
		return getDefault().fromJson(json, classOfT);
	}

	public static <T> T fromJson(Reader json, Type typeOfT) throws JsonIOException, JsonSyntaxException
	{
		return getDefault().fromJson(json, typeOfT);
	}

	public static <T> T fromJson(JsonReader reader, Type typeOfT) throws JsonIOException, JsonSyntaxException
	{
		return getDefault().fromJson(reader, typeOfT);
	}

	public static <T> T fromJson(JsonElement json, Class<T> classOfT) throws JsonSyntaxException
	{
		return getDefault().fromJson(json, classOfT);
	}

	public static <T> T fromJson(JsonElement json, Type typeOfT) throws JsonSyntaxException
	{
		return getDefault().fromJson(json, typeOfT);
	}
}
