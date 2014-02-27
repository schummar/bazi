package de.uni_augsburg.bazi.common.format;

import de.uni_augsburg.bazi.common.PluginManager;
import de.uni_augsburg.bazi.common.util.MList;
import de.uni_augsburg.bazi.common.Resources;
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
	static final Map<Class<?>, ObjectConverter<?>> SERIALIZERS = new HashMap<>(), DESERIALIZERS = new HashMap<>();

	static
	{
		registerAdapter(Real.class, (StringConverter<Real>) Real::valueOf);
		registerAdapter(Rational.class, (StringConverter<Rational>) Rational::valueOf);
		registerAdapter(Int.class, (StringConverter<Int>) Int::valueOf);
	}

	public static <T> void registerAdapter(Class<T> type, ObjectConverter<? super T> adapter)
	{
		SERIALIZERS.put(type, adapter);
		DESERIALIZERS.put(type, adapter);
	}


	public static <T> T deserialize(Object value, Class<T> type)
	{
		return deserialize(value, type, null);
	}

	public static <T> T deserialize(Object value, Class<T> type, Converter attributeConverter)
	{
		if (!DESERIALIZERS.containsKey(type))
			DESERIALIZERS.put(type, buildAdapter(type, attributeConverter, true));

		@SuppressWarnings("unchecked")
		T t = (T) DESERIALIZERS.get(type).deserialize(value);
		return t;
	}


	public static <T> Object serialize(T value) { return serialize(value, null); }

	public static <T> Object serialize(T value, Converter attributeConverter)
	{
		if (value == null) return null;
		if (!SERIALIZERS.containsKey(value.getClass()))
			SERIALIZERS.put(value.getClass(), buildAdapter(value.getClass(), attributeConverter, false));

		@SuppressWarnings("unchecked")
		ObjectConverter<T> converter = ((ObjectConverter<T>) SERIALIZERS.get(value.getClass()));
		return converter.serialize(value);
	}

	static ObjectConverter<?> buildAdapter(Class<?> type, Converter attributeConverter, boolean isDeserializer)
	{
		if (attributeConverter != null)
		{
			try
			{
				return attributeConverter.value().newInstance();
			}
			catch (InstantiationException | IllegalAccessException e)
			{
				LOGGER.error(Resources.get("converter.instatiation", attributeConverter.value()));
			}
		}

		Converter classConverter = type.getAnnotation(Converter.class);
		if (classConverter != null)
		{
			try
			{
				return classConverter.value().newInstance();
			}
			catch (InstantiationException | IllegalAccessException e)
			{
				LOGGER.error(Resources.get("converter.instatiation", classConverter.value()));
			}
		}

		if (PluginManager.INSTANCE.getPluginsOfInstanceType(type).size() > 0)
		{
			return new PluginConverter<>(type);
		}


		MList<Class<?>> candidates;
		if (isDeserializer)
		{
			candidates = new MList<>(DESERIALIZERS.keySet());
			candidates.removeIf(t -> !type.isAssignableFrom(t));
			candidates.sort(
				(a, b) -> {
					if (a.isAssignableFrom(b)) return -1;
					if (b.isAssignableFrom(a)) return 1;
					if (SERIALIZERS.get(a) instanceof SimpleConverter<?>) return 1;
					if (SERIALIZERS.get(b) instanceof SimpleConverter<?>) return -1;
					return 0;
				}
			);
		}
		else
		{
			candidates = new MList<>(SERIALIZERS.keySet());
			candidates.removeIf(t -> !t.isAssignableFrom(type));
			candidates.sort(
				(a, b) -> {
					if (a.isAssignableFrom(b)) return 1;
					if (b.isAssignableFrom(a)) return -1;
					if (SERIALIZERS.get(a) instanceof SimpleConverter<?>) return 1;
					if (SERIALIZERS.get(b) instanceof SimpleConverter<?>) return -1;
					return 0;
				}
			);
		}
		if (candidates.size() > 0)
		{
			return isDeserializer
				? DESERIALIZERS.get(candidates.get(0))
				: SERIALIZERS.get(candidates.get(0));
		}

		if (!isDeserializer && !type.equals(Object.class))
		{
			if (type.getSuperclass() != null)
				SERIALIZERS.put(type.getSuperclass(), buildAdapter(type.getSuperclass(), null, false));
			for (Class<?> si : type.getInterfaces())
				SERIALIZERS.put(si, buildAdapter(si, null, false));
			return buildAdapter(type, null, false);
		}

		return new SimpleConverter<>();
	}
}
