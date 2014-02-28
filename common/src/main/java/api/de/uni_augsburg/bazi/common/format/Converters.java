package de.uni_augsburg.bazi.common.format;

import de.uni_augsburg.bazi.common.PluginManager;
import de.uni_augsburg.bazi.common.Resources;
import de.uni_augsburg.bazi.common.util.MList;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Rational;
import de.uni_augsburg.bazi.math.Real;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class Converters
{
	static final Logger LOGGER = LoggerFactory.getLogger(Converters.class);
	static final Map<Class<?>, ObjectConverter<?>>
		ADAPTERS = new HashMap<>(),
		SERIALIZERS = new HashMap<>(),
		DESERIALIZERS = new HashMap<>();


	static
	{
		registerAdapter(Real.class, (StringConverter<Real>) Real::valueOf);
		registerAdapter(Rational.class, (StringConverter<Rational>) Rational::valueOf);
		registerAdapter(Int.class, (StringConverter<Int>) Int::valueOf);
	}


	public static <T> void registerAdapter(Class<T> type, Converter converter)
	{
		if (converter == null || converter.value().isInstance(ADAPTERS.get(type))) return;
		try
		{
			@SuppressWarnings("unchecked")
			ObjectConverter<? super T> adapter = (ObjectConverter<? super T>) converter.value().newInstance();
			registerAdapter(type, adapter);
		}
		catch (InstantiationException | IllegalAccessException e)
		{
			e.printStackTrace();
		}
	}
	public static <T> void registerAdapter(Class<T> type, ObjectConverter<? super T> adapter)
	{
		ADAPTERS.put(type, adapter);
		SERIALIZERS.clear();
		SERIALIZERS.putAll(ADAPTERS);
		DESERIALIZERS.clear();
		DESERIALIZERS.putAll(ADAPTERS);
	}


	public static <T> Object serialize(T value, Converter attributeConverter)
	{
		registerAdapter(value.getClass(), attributeConverter);
		return serialize(value.getClass(), value);
	}
	public static <T> Object serialize(T value)
	{
		return serialize(value.getClass(), value);
	}
	private static <T> Object serialize(Class<T> type, Object value)
	{
		@SuppressWarnings("unchecked") // only called with (value.getClass(), value, ...)
			T tValue = (T) value;
		return getSerializer(type).serialize(tValue);
	}


	public static <T> T deserialize(Object value, Class<T> type, Converter attributeConverter)
	{
		registerAdapter(type, attributeConverter);
		return deserialize(value, type);
	}
	public static <T> T deserialize(Object value, Class<T> type)
	{
		return getDeserializer(type).deserialize(value);
	}


	public static <T> ObjectConverter<T> getSerializer(Class<T> type)
	{
		if (!SERIALIZERS.containsKey(type)) refreshAdapters(type);
		@SuppressWarnings("unchecked")
		ObjectConverter<T> converter = (ObjectConverter<T>) SERIALIZERS.get(type);
		return converter != null ? converter : new SimpleConverter<>();
	}


	public static <T> ObjectConverter<T> getDeserializer(Class<T> type)
	{
		if (!DESERIALIZERS.containsKey(type)) refreshAdapters(type);
		@SuppressWarnings("unchecked")
		ObjectConverter<T> converter = (ObjectConverter<T>) DESERIALIZERS.get(type);
		return converter != null ? converter : new SimpleConverter<>();
	}


	private static void refreshAdapters(Class<?> type)
	{
		if (SERIALIZERS.containsKey(type) && DESERIALIZERS.containsKey(type)) return;

		Converter classConverter = type.getAnnotation(Converter.class);
		if (classConverter != null)
		{
			try
			{
				ObjectConverter<?> adapter = classConverter.value().newInstance();
				SERIALIZERS.putIfAbsent(type, adapter);
				DESERIALIZERS.putIfAbsent(type, adapter);
				return;
			}
			catch (InstantiationException | IllegalAccessException e)
			{
				LOGGER.error(Resources.get("converter.instatiation", classConverter.value()));
			}
		}

		if (PluginManager.INSTANCE.getPluginsOfInstanceType(type).size() > 0)
		{
			PluginConverter<?> adapter = new PluginConverter<>(type);
			SERIALIZERS.putIfAbsent(type, adapter);
			DESERIALIZERS.putIfAbsent(type, adapter);
			return;
		}


		if (!SERIALIZERS.containsKey(type))
		{
			if (type.getSuperclass() != null)
				refreshAdapters(type.getSuperclass());
			for (Class<?> si : type.getInterfaces())
				refreshAdapters(si);
		}


		if (!SERIALIZERS.containsKey(type))
		{
			MList<Class<?>> candidates = new MList<>(SERIALIZERS.keySet());
			candidates.removeIf(t -> !t.isAssignableFrom(type));
			candidates.sort(
				(a, b) -> {
					if (a.isAssignableFrom(b)) return 1;
					if (b.isAssignableFrom(a)) return -1;
					if (SERIALIZERS.get(a) == null) return 1;
					if (SERIALIZERS.get(b) == null) return -1;
					return 0;
				}
			);
			if (candidates.size() > 0)
				SERIALIZERS.put(type, SERIALIZERS.get(candidates.get(0)));
		}


		if (!DESERIALIZERS.containsKey(type))
		{
			MList<Class<?>> candidates = new MList<>(DESERIALIZERS.keySet());
			candidates.removeIf(t -> !type.isAssignableFrom(t));
			candidates.sort(
				(a, b) -> {
					if (a.isAssignableFrom(b)) return -1;
					if (b.isAssignableFrom(a)) return 1;
					if (DESERIALIZERS.get(a) == null) return 1;
					if (DESERIALIZERS.get(b) == null) return -1;
					return 0;
				}
			);
			if (candidates.size() > 0)
				DESERIALIZERS.put(type, DESERIALIZERS.get(candidates.get(0)));
		}

		SERIALIZERS.putIfAbsent(type, null);
		DESERIALIZERS.putIfAbsent(type, null);
	}
}
