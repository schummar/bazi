package de.uni_augsburg.bazi.common.format;

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
	static final Map<Class<?>, ObjectConverter<?>> ADAPTERS = new HashMap<>();

	static
	{
		ADAPTERS.put(Real.class, (StringConverter<Real>) Real::valueOf);
		ADAPTERS.put(Rational.class, (StringConverter<Rational>) Rational::valueOf);
		ADAPTERS.put(Int.class, (StringConverter<Int>) Int::valueOf);
	}


	public static <T> void registerAdapter(Class<T> type, ObjectConverter<? super T> adapter)
	{
		ADAPTERS.put(type, adapter);
	}

	public static <T> T deserialize(Object value, Class<T> type)
	{
		return deserialize(value, type, null);
	}

	public static <T> T deserialize(Object value, Class<T> type, Converter attributeConverter)
	{
		if (!ADAPTERS.containsKey(type)) buildAdapter(type, attributeConverter);
		@SuppressWarnings("unchecked")
		T t = (T) ADAPTERS.get(type).deserialize(value);
		return t;
	}

	static void buildAdapter(Class<?> type, Converter attributeConverter)
	{
		if (attributeConverter != null)
		{
			try
			{
				ADAPTERS.put(type, attributeConverter.value().newInstance());
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
				ADAPTERS.put(type, classConverter.value().newInstance());
				return;
			}
			catch (InstantiationException | IllegalAccessException e)
			{
				LOGGER.error(Resources.get("converter.instatiation", classConverter.value()));
			}
		}

		MList<Class<?>> candidates = new MList<>(ADAPTERS.keySet());
		if (candidates.size() > 0)
		{
			candidates.removeIf(t -> !type.isAssignableFrom(t));
			candidates.sort(
				(a, b) -> {
					if (a.isAssignableFrom(b)) return -1;
					if (b.isAssignableFrom(a)) return 1;
					return 0;
				}
			);
			ADAPTERS.put(type, ADAPTERS.get(candidates.get(0)));
			return;
		}

		ADAPTERS.put(type, (StringConverter<?>) s -> null);
	}
}
