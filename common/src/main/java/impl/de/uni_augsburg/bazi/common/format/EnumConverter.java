package de.uni_augsburg.bazi.common.format;

import de.uni_augsburg.bazi.common.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class EnumConverter<T extends Enum<T>> implements ObjectConverter<T>
{
	private static final Logger LOGGER = LoggerFactory.getLogger(EnumConverter.class);


	public static <T extends Enum<T>> EnumConverter<T> of(Class<?> type)
	{
		if (!ConvertibleEnum.class.isAssignableFrom(type)
			|| !type.isEnum())
			throw new IllegalArgumentException();
		@SuppressWarnings("unchecked")
		EnumConverter<T> converter = new EnumConverter<>((Class<T>) type);
		return converter;
	}


	private final Class<T> type;
	public EnumConverter(Class<T> type)
	{
		this.type = type;

		if (!ConvertibleEnum.class.isAssignableFrom(type)) return;
		for (T t : type.getEnumConstants())
		{
			ConvertibleEnum cet = (ConvertibleEnum) t;
			if (!cet.matches(cet.key()))
				LOGGER.warn(Resources.get("warn.enum_key_match_pattern", t));
		}

	}


	@Override public Object serialize(T value)
	{
		if (value instanceof ConvertibleEnum)
			return ((ConvertibleEnum) value).key();
		return value.name();
	}


	@Override public T deserialize(Object value)
	{
		if (!ConvertibleEnum.class.isAssignableFrom(type))
			return Enum.valueOf(type, value.toString());

		String name = value.toString().toLowerCase();
		for (T t : type.getEnumConstants())
			if (((ConvertibleEnum) t).matches(name))
				return t;

		LOGGER.warn(Resources.get("warn.unknown_value_enum", name, type.getName()));
		return type.getEnumConstants()[0];
	}
}
