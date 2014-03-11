package de.uni_augsburg.bazi.common.format;

import de.uni_augsburg.bazi.common.Plugin;
import de.uni_augsburg.bazi.common.Resources;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Rational;
import de.uni_augsburg.bazi.math.Real;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
		registerAdapter(Integer.class, (StringConverter<Integer>) Integer::valueOf);
		registerAdapter(Integer.class, (StringConverter<Integer>) Integer::valueOf);
		registerAdapter(Long.class, (StringConverter<Long>) Long::valueOf);
		registerAdapter(long.class, (StringConverter<Long>) Long::valueOf);
		registerAdapter(Float.class, (StringConverter<Float>) Float::valueOf);
		registerAdapter(float.class, (StringConverter<Float>) Float::valueOf);
		registerAdapter(Double.class, (StringConverter<Double>) Double::valueOf);
		registerAdapter(double.class, (StringConverter<Double>) Double::valueOf);
		registerAdapter(Boolean.class, (StringConverter<Boolean>) Boolean::valueOf);
		registerAdapter(boolean.class, (StringConverter<Boolean>) Boolean::valueOf);
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


	private static synchronized void refreshAdapters(Class<?> type)
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

		if (Plugin.Instance.class.isAssignableFrom(type))
		{
			@SuppressWarnings("unchecked")
			PluginConverter<?> adapter = new PluginConverter<>((Class<? extends Plugin.Instance>) type);
			SERIALIZERS.putIfAbsent(type, adapter);
			DESERIALIZERS.putIfAbsent(type, adapter);
			return;
		}

		if (type.isEnum() && ConvertibleEnum.class.isAssignableFrom(type))
		{
			EnumConverter adapter = EnumConverter.of(type);
			SERIALIZERS.putIfAbsent(type, adapter);
			DESERIALIZERS.putIfAbsent(type, adapter);
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
			List<Class<?>> candidates = new ArrayList<>(SERIALIZERS.keySet());
			candidates.removeIf(t -> !t.isAssignableFrom(type));
			candidates.sort(
				(a, b) -> {
					if (a.isAssignableFrom(b)) return 1;
					if (b.isAssignableFrom(a)) return -1;
					if (SERIALIZERS.get(a) == null && SERIALIZERS.get(b) != null) return 1;
					if (SERIALIZERS.get(b) == null && SERIALIZERS.get(a) != null) return -1;
					return 0;
				}
			);
			if (candidates.size() > 0)
				SERIALIZERS.put(type, SERIALIZERS.get(candidates.get(0)));
		}


		if (!DESERIALIZERS.containsKey(type))
		{
			List<Class<?>> candidates = new ArrayList<>(DESERIALIZERS.keySet());
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
