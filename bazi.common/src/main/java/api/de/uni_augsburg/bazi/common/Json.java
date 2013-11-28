package de.uni_augsburg.bazi.common;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
				JsonObject jo = currentElement.getAsJsonObject();
				for (Entry<String, JsonElement> entry : jo.entrySet())
				{
					String key = entry.getKey();
					boolean found = false;
					for (Field field : ((Class<?>) currentType.getType()).getFields())
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


	// //////////////////////////////////////////////////////////////////////////


	public static Gson createGson()
	{
		return new GsonBuilder()
				.setPrettyPrinting()
				.registerTypeAdapterFactory(TYPE_ADAPTER_FACTORY)
				.create();
	}

	private static final TypeAdapterFactory TYPE_ADAPTER_FACTORY = new TypeAdapterFactory()
	{
		@Override public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type)
		{
			Adapter<T> adapter = getAdapterForType(type.getRawType());
			if (adapter != null)
				return adapter.nullSafe();

			return null;
		}
	};


	// //////////////////////////////////////////////////////////////////////////


	@Retention(RetentionPolicy.RUNTIME) public static @interface JsonAdapter
	{}

	public static class Adapter<T> extends TypeAdapter<T>
	{
		public Class<? extends T> getPlainType()
		{
			return null;
		}

		@Override public void write(JsonWriter out, T value) throws IOException
		{
			new GsonBuilder().setPrettyPrinting().create().toJson(value, value.getClass(), out);
		}

		@Override public T read(JsonReader in) throws IOException
		{
			return createGson().fromJson(in, getPlainType());
		}
	}

	@SuppressWarnings("unchecked") public static <T> Adapter<T> getAdapterForType(Class<? super T> type)
	{
		for (Field field : type.getFields())
		{
			if (field.isAnnotationPresent(JsonAdapter.class)
					&& Modifier.isPublic(field.getModifiers())
					&& Modifier.isStatic(field.getModifiers())
					&& Adapter.class.isAssignableFrom(field.getType())
					&& type.isAssignableFrom((Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0]))
				try
				{
					return (Adapter<T>) field.get(null);
				}
				catch (IllegalArgumentException | IllegalAccessException e)
				{}
		}

		return null;
	}
}
