package de.uni_augsburg.bazi.common;

import com.google.gson.*;
import com.google.gson.internal.GsonTypes;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

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
import java.util.*;
import java.util.Map.Entry;

public enum Json
{
	INSTANCE;

	public final Gson GSON;

	Json()
	{
		GSON = new GsonBuilder()
			.setPrettyPrinting()
			.registerTypeHierarchyAdapter(DefinesSerializer.class, (JsonSerializer<DefinesSerializer>) (src, type, context) -> src.serialize(context))
			.registerTypeAdapterFactory(TYPE_ADAPTER_FACTORY)
			.create();
	}

	private final TypeAdapterFactory TYPE_ADAPTER_FACTORY = new TypeAdapterFactory()
	{
		private final Map<TypeToken<?>, TypeAdapter<?>> adapters = new HashMap<>();

		@Override
		public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type)
		{
			@SuppressWarnings("unchecked")
			TypeAdapter<T> adapter = (TypeAdapter<T>) adapters.get(type);
			if (adapter == null)
				adapters.put(type, adapter = createAdapter(gson, type));
			return adapter;
		}

		private <T> TypeAdapter<T> createAdapter(Gson gson, TypeToken<T> type)
		{
			TypeToken<T> delegate = getDeserializeAsClass(type);
			if (delegate != null)
				return gson.getAdapter(delegate);

			JsonSerializer<T> serializer = getSerializer(type);
			JsonDeserializer<T> deserializer = getDeserializer(type);
			if (serializer == null && deserializer == null) return gson.getDelegateAdapter(this, type);

			GsonBuilder temp = new GsonBuilder();
			if (serializer == null)
				temp.registerTypeAdapter(type.getType(), deserializer);
			if (deserializer == null)
				temp.registerTypeAdapter(type.getType(), serializer);
			else
				temp.registerTypeAdapter(type.getType(), new Adapter<>(serializer, deserializer));
			return temp.create().getAdapter(type);
		}
	};

	private static class Adapter<T> implements JsonSerializer<T>, JsonDeserializer<T>
	{
		private final JsonSerializer<T> serializer;
		private final JsonDeserializer<T> deserializer;

		private Adapter(JsonSerializer<T> serializer, JsonDeserializer<T> deserializer)
		{
			this.serializer = serializer;
			this.deserializer = deserializer;
		}

		@Override
		public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
		{
			return deserializer.deserialize(json, typeOfT, context);
		}

		@Override
		public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context)
		{
			return serializer.serialize(src, typeOfSrc, context);
		}
	}

	// //////////////////////////////////////////////////////////////////////////
	// Annotations, Interfaces, Exctractors                                    //
	// //////////////////////////////////////////////////////////////////////////

	public static interface DefinesSerializer
	{
		public JsonElement serialize(JsonSerializationContext context);
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public static @interface SerializeAsString
	{}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public static @interface DeserializeFromString
	{}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public static @interface DeserializeAsClass
	{
		Class<?> value();
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public static @interface Serialize
	{
		Class<? extends JsonSerializer<?>> value();
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public static @interface Deserialize
	{
		Class<? extends JsonDeserializer<?>> value();
	}


	@SuppressWarnings("unchecked")
	public static <T> TypeToken<T> getDeserializeAsClass(TypeToken<T> type)
	{
		if (!type.getRawType().isAnnotationPresent(DeserializeAsClass.class)) return null;
		return TypeToken.get((Class<T>) type.getRawType().getAnnotation(DeserializeAsClass.class).value());
	}


	@SuppressWarnings("unchecked")
	public static <T> JsonSerializer<T> getSerializer(TypeToken<T> type)
	{
		if (type.getRawType().isAnnotationPresent(SerializeAsString.class))
		{
			return (src, t, context) -> new JsonPrimitive(src.toString());
		}
		else if (type.getRawType().isAnnotationPresent(Serialize.class))
		{
			try
			{
				return (JsonSerializer<T>) type.getRawType().getAnnotation(Serialize.class).value().newInstance();
			}
			catch (InstantiationException | IllegalAccessException e)
			{}
		}
		return null;
	}


	@SuppressWarnings("unchecked")
	public static <T> JsonDeserializer<T> getDeserializer(TypeToken<T> type)
	{
		if (type.getRawType().isAnnotationPresent(DeserializeFromString.class))
		{
			try
			{
				Method m = type.getRawType().getMethod("valueOf", String.class);
				return (json, t, context) -> {
					try
					{
						return (T) m.invoke(null, json.getAsString());
					}
					catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException e)
					{}
					return null;
				};
			}
			catch (NoSuchMethodException | SecurityException e)
			{
				e.printStackTrace();
			}
		}
		else if (type.getRawType().isAnnotationPresent(Deserialize.class))
		{
			try
			{
				return (JsonDeserializer<T>) type.getRawType().getAnnotation(Deserialize.class).value().newInstance();
			}
			catch (InstantiationException | IllegalAccessException e)
			{}
		}
		return null;
	}


	// //////////////////////////////////////////////////////////////////////////
	// Validator                                                               //
	// //////////////////////////////////////////////////////////////////////////

	public static <T> List<String> checkJson(String json, Class<T> type)
	{
		return checkJson(json, TypeToken.<T>get(type));
	}

	public static <T> List<String> checkJson(String json, TypeToken<T> type)
	{
		JsonReader reader = new JsonReader(new StringReader(json));
		reader.setLenient(false);
		JsonElement root = new JsonParser().parse(reader);
		return checkJson(root, type);
	}

	public static <T> List<String> checkJson(JsonElement element, TypeToken<T> type)
	{
		List<Tuple<JsonElement, ? extends TypeToken<?>>> queue = new ArrayList<>();
		queue.add(Tuple.of(element, type));

		List<String> warnings = new ArrayList<>();

		while (!queue.isEmpty())
		{
			Tuple<JsonElement, ? extends TypeToken<?>> tuple = queue.remove(0);
			JsonElement currentElement = tuple.x();
			TypeToken<?> currentType = tuple.y();

			if (currentElement.isJsonNull() || currentElement.isJsonPrimitive())
			{
				try (JsonReader reader = new JsonReader(new StringReader(currentElement.getAsString())))
				{
					reader.setLenient(true);
					if (INSTANCE.GSON.getAdapter(currentType).fromJsonTree(currentElement) != null)
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
					childType = GsonTypes.getArrayComponentType(currentType.getType());
				else if (Collection.class.isAssignableFrom(currentType.getRawType()))
					childType = GsonTypes.getCollectionElementType(currentType.getType(), currentType.getRawType());

				if (childType == null)
				{
					try (JsonReader reader = new JsonReader(new StringReader(ja.toString())))
					{
						reader.setLenient(true);
						if (INSTANCE.GSON.getAdapter(currentType).fromJsonTree(currentElement) != null)
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
						queue.add(Tuple.of(entry, TypeToken.get(childType)));
				}
			}
			else if (currentElement.isJsonObject())
			{
				if (getDeserializer(currentType) != null)
					continue;

				TypeToken<?> currentClass = getDeserializeAsClass(currentType);
				if (currentClass == null)
					currentClass = currentType;

				JsonObject jo = currentElement.getAsJsonObject();
				for (Entry<String, JsonElement> entry : jo.entrySet())
				{
					String key = entry.getKey();
					boolean found = false;
					for (Field field : currentClass.getRawType().getFields())
						if (field.getName().equals(key))
						{
							found = true;
							queue.add(Tuple.of(entry.getValue(), TypeToken.get(field.getGenericType())));
							break;
						}
					if (!found)
						warnings.add(Resources.get("json.unknown_key", key, currentType.getType()));
				}
			}
		}

		return warnings;
	}


	// //////////////////////////////////////////////////////////////////////////
	// Gson delegate                                                           //
	// //////////////////////////////////////////////////////////////////////////

	public static <T> TypeAdapter<T> getAdapter(TypeToken<T> type)
	{
		return INSTANCE.GSON.getAdapter(type);
	}

	public static <T> TypeAdapter<T> getDelegateAdapter(TypeAdapterFactory skipPast, TypeToken<T> type)
	{
		return INSTANCE.GSON.getDelegateAdapter(skipPast, type);
	}

	public static <T> TypeAdapter<T> getAdapter(Class<T> type)
	{
		return INSTANCE.GSON.getAdapter(type);
	}

	public static JsonElement toJsonTree(Object src)
	{
		return INSTANCE.GSON.toJsonTree(src);
	}

	public static JsonElement toJsonTree(Object src, Type typeOfSrc)
	{
		return INSTANCE.GSON.toJsonTree(src, typeOfSrc);
	}

	public static String toJson(Object src)
	{
		return INSTANCE.GSON.toJson(src);
	}

	public static String toJson(Object src, Type typeOfSrc)
	{
		return INSTANCE.GSON.toJson(src, typeOfSrc);
	}

	public static void toJson(Object src, Appendable writer) throws JsonIOException
	{
		INSTANCE.GSON.toJson(src, writer);
	}

	public static void toJson(Object src, Type typeOfSrc, Appendable writer) throws JsonIOException
	{
		INSTANCE.GSON.toJson(src, typeOfSrc, writer);
	}

	public static void toJson(Object src, Type typeOfSrc, JsonWriter writer) throws JsonIOException
	{
		INSTANCE.GSON.toJson(src, typeOfSrc, writer);
	}

	public static String toJson(JsonElement jsonElement)
	{
		return INSTANCE.GSON.toJson(jsonElement);
	}

	public static void toJson(JsonElement jsonElement, Appendable writer) throws JsonIOException
	{
		INSTANCE.GSON.toJson(jsonElement, writer);
	}

	public static void toJson(JsonElement jsonElement, JsonWriter writer) throws JsonIOException
	{
		INSTANCE.GSON.toJson(jsonElement, writer);
	}

	public static <T> T fromJson(String json, Class<T> classOfT) throws JsonSyntaxException
	{
		return INSTANCE.GSON.fromJson(json, classOfT);
	}

	public static <T> T fromJson(String json, Type typeOfT) throws JsonSyntaxException
	{
		return INSTANCE.GSON.fromJson(json, typeOfT);
	}

	public static <T> T fromJson(Reader json, Class<T> classOfT) throws JsonSyntaxException, JsonIOException
	{
		return INSTANCE.GSON.fromJson(json, classOfT);
	}

	public static <T> T fromJson(Reader json, Type typeOfT) throws JsonIOException, JsonSyntaxException
	{
		return INSTANCE.GSON.fromJson(json, typeOfT);
	}

	public static <T> T fromJson(JsonReader reader, Type typeOfT) throws JsonIOException, JsonSyntaxException
	{
		return INSTANCE.GSON.fromJson(reader, typeOfT);
	}

	public static <T> T fromJson(JsonElement json, Class<T> classOfT) throws JsonSyntaxException
	{
		return INSTANCE.GSON.fromJson(json, classOfT);
	}

	public static <T> T fromJson(JsonElement json, Type typeOfT) throws JsonSyntaxException
	{
		return INSTANCE.GSON.fromJson(json, typeOfT);
	}
}
