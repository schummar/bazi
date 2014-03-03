package de.uni_augsburg.bazi.common.data;

import de.uni_augsburg.bazi.common.format.Converters;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.List;
import java.util.Map;

/**
 * Created by Marco on 03.03.14.
 */
public class CastHelper
{
	@SuppressWarnings("unchecked")
	public static <T> T cast(Object value, Type type)
	{
		try
		{
			if (value == null || type == null) return (T) value;


			Class<?> raw = raw(type);

			// Data instance
			if (Data.class.isAssignableFrom(raw))
			{
				if (value instanceof Data)
					return (T) ((Data) value).cast((Class<? extends Data>) raw);
				if (value instanceof Map)
					return (T) new MapData((Map) value).cast((Class<? extends Data>) raw);
			}

			// List
			if (List.class.isAssignableFrom(raw) && value instanceof List)
			{
				return (T) new CastList<>((List) value, param(type, 0));
			}

			// Map
			if (Map.class.isAssignableFrom(raw))
			{
				if (param(type, 0).equals(String.class) && param(type, 1).equals(Object.class) && value instanceof Data)
					return (T) ((Data) value).serialize();
				if (value instanceof Map)
					return (T) new CastMap<>((Map) value, param(type, 1));
			}

			if (raw.isInstance(value)) return (T) value;
			return (T) Converters.deserialize(value, raw);
		}
		catch (Exception e)
		{
			throw new IncompatibleTypesException(String.format("cannot cast %s to %s", value.getClass(), raw(type)));
		}
	}


	public static Class<?> raw(Type type)
	{
		if (type instanceof Class<?>) return (Class<?>) type;
		if (type instanceof ParameterizedType) return (Class<?>) ((ParameterizedType) type).getRawType();
		if (type instanceof WildcardType && ((WildcardType) type).getUpperBounds().length > 0)
			return (Class<?>) ((WildcardType) type).getUpperBounds()[0];
		return Object.class;
	}


	public static Type param(Type type, int index)
	{
		return ((ParameterizedType) type).getActualTypeArguments()[index];
	}


	public static class IncompatibleTypesException extends RuntimeException
	{
		public IncompatibleTypesException()
		{
			super();
		}
		public IncompatibleTypesException(String message)
		{
			super(message);
		}
		public IncompatibleTypesException(String message, Throwable cause)
		{
			super(message, cause);
		}
		public IncompatibleTypesException(Throwable cause)
		{
			super(cause);
		}
	}
}
