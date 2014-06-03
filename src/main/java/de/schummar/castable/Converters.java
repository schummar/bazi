package de.schummar.castable;

import javafx.beans.property.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.HashMap;
import java.util.Map;

public abstract class Converters
{
	private static Map<Method, Converter> converterCache = new HashMap<>();
	public static Converter get(Method method)
	{
		try
		{
			Converter converter = converterCache.get(method);
			if (converter != null) return converter;

			Attribute attribute = method.getAnnotation(Attribute.class);
			if (attribute.converter() != Converter.class) converter = attribute.converter().newInstance();
			else
			{
				Type type = method.getReturnType().isAssignableFrom(Property.class)
					? ((ParameterizedType) method.getGenericReturnType()).getActualTypeArguments()[0]
					: method.getGenericReturnType();
				Converter contentconverter = null;
				if (attribute.contentConverter() != Converter.class) contentconverter = attribute.contentConverter().newInstance();
				converter = get(type, contentconverter);
			}
			converterCache.put(method, converter);
			return converter;
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	public static Converter<?> get(Type type, Converter<?> contentconverter) throws IllegalAccessException, InstantiationException
	{
		Class<?> c = classOf(type);

		if (c.isAnnotationPresent(Convert.class))
			return c.getAnnotation(Convert.class).value().newInstance();

		if (c == Integer.class || c == int.class) return INTEGER_OBJ_converter;
		if (c == Long.class || c == long.class) return LONG_OBJ_converter;
		if (c == Float.class || c == float.class) return FLOAT_OBJ_converter;
		if (c == Double.class || c == double.class) return DOUBLE_OBJ_converter;
		if (c == String.class) return STRING_OBJ_converter;
		if (Enum.class.isAssignableFrom(c)) return EnumConverter.of(c);

		if (c.isAssignableFrom(CList.class))
		{
			Type contentType = ((ParameterizedType) type).getActualTypeArguments()[0];
			if (contentconverter == null)
				contentconverter = get(contentType, null);

			return c.isAssignableFrom(DataList.class)
				? create(contentconverter, classOf(contentType))
				: new Listconverter<>(contentconverter);
		}

		if (Data.class.isAssignableFrom(c) && c.isInterface())
		{
			@SuppressWarnings("unchecked")
			Class<? extends Data> d = (Class<? extends Data>) c;
			return new Mapconverter<>(d);
		}

		throw new RuntimeException(String.format("No converter for type %s available.", type));
	}


	public static class Listconverter<T> implements Converter<CList<T>>
	{
		private final Converter<T> converter;
		public Listconverter(Converter<T> converter)
		{
			this.converter = converter;
		}
		@Override public CList<T> apply(Castable o)
		{
			return new CList<>(o.asCastableList(), converter);
		}
		@Override public Castable applyInverse(CList<T> ts)
		{
			CastableList o = new CastableList();
			if (ts != null && !ts.isEmpty())
				new CList<>(o, converter).addAll(ts);
			return o;
		}
	}

	public static <T extends Data> DataListconverter<T> create(Converter<?> converter, Class<?> type)
	{
		@SuppressWarnings("unchecked") Converter<T> tConverter = (Converter<T>) converter;
		@SuppressWarnings("unchecked") Class<T> tType = (Class<T>) type;
		return new DataListconverter<>(tConverter, tType);
	}
	public static class DataListconverter<T extends Data> implements Converter<DataList<T>>
	{
		private final Converter<T> converter;
		private final Class<T> type;
		public DataListconverter(Converter<T> converter, Class<T> type)
		{
			this.converter = converter;
			this.type = type;
		}
		@Override public DataList<T> apply(Castable o)
		{
			return new DataList<>(o.asCastableList(), converter, type);
		}
		@Override public Castable applyInverse(DataList<T> ts)
		{
			CastableList o = new CastableList();
			if (ts != null && !ts.isEmpty())
				new DataList<>(o, converter, type).addAll(ts);
			return o;
		}
	}


	public static class Mapconverter<T extends Data> implements Converter<T>
	{
		private final Class<T> c;
		public Mapconverter(Class<T> c)
		{
			this.c = c;
		}
		@Override public T apply(Castable o)
		{
			return o.asCastableObject().cast(c);
		}
		@Override public Castable applyInverse(T t)
		{
			if (t == null) return new CastableObject();

			CastableObject o = new CastableObject();
			for (Method method : t.getClass().getMethods())
			{
				try { method = c.getMethod(method.getName(), method.getParameterTypes()); } // no annotations on proxy
				catch (NoSuchMethodException ignore) { }
				if (!method.isAnnotationPresent(Attribute.class)) continue;
				try
				{
					@SuppressWarnings("unchecked")
					Property<Object> prop = o.getProperty(method);
					Object v = method.invoke(t);
					if (v instanceof Property) v = ((Property) v).getValue();
					prop.setValue(v);
				}
				catch (Exception ignore) {}
			}
			return o;
		}
	}

	public static class EnumConverter<T extends Enum<T>> implements Converter<T>
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
					LOGGER.error(String.format("The enum value %s in does not match its own key.", t));
			}

		}

		@Override public T apply(Castable o)
		{
			if (!ConvertibleEnum.class.isAssignableFrom(type))
				return Enum.valueOf(type, o.asCastableString().getValue());

			String name = o.asCastableString().getValue().toLowerCase();
			for (T t : type.getEnumConstants())
				if (((ConvertibleEnum) t).matches(name))
					return t;

			LOGGER.error(String.format("No enum value in %s matches '%s'.", type, o));
			return type.getEnumConstants()[0];
		}
		@Override public Castable applyInverse(T t)
		{
			if (t instanceof ConvertibleEnum)
				return new CastableString(((ConvertibleEnum) t).key());
			return new CastableString(t.name());
		}
	}

	public static final Converter<Integer> INTEGER_OBJ_converter = new Converter<Integer>()
	{
		@Override public Integer apply(Castable o)
		{
			try
			{
				String s = o.asCastableString().getValue();
				if (s.contains(".")) s = s.substring(0, s.indexOf("."));
				if (s.isEmpty()) return 0;
				return Integer.parseInt(s);
			}
			catch (Exception ignore) {}
			return 0;
		}
		@Override public Castable applyInverse(Integer integer)
		{
			return new CastableString(integer == null ? "" : integer.toString());
		}
	};

	public static final Converter<Long> LONG_OBJ_converter = new Converter<Long>()
	{
		@Override public Long apply(Castable o)
		{
			try
			{
				String s = o.asCastableString().getValue();
				if (s.contains(".")) s = s.substring(0, s.indexOf("."));
				if (s.isEmpty()) return 0l;
				return Long.parseLong(s);
			}
			catch (Exception ignore) {}
			return 0l;
		}
		@Override public Castable applyInverse(Long l)
		{
			return new CastableString(l == null ? "" : l.toString());
		}
	};

	public static final Converter<Float> FLOAT_OBJ_converter = new Converter<Float>()
	{
		@Override public Float apply(Castable o)
		{
			try { return Float.parseFloat(o.asCastableString().getValue()); }
			catch (Exception ignore) {}
			return 0f;
		}
		@Override public Castable applyInverse(Float f)
		{
			return new CastableString(f == null ? "" : f.toString());
		}
	};

	public static final Converter<Double> DOUBLE_OBJ_converter = new Converter<Double>()
	{
		@Override public Double apply(Castable o)
		{
			try { return Double.parseDouble(o.asCastableString().getValue()); }
			catch (Exception ignore) {}
			return 0d;
		}
		@Override public Castable applyInverse(Double d)
		{
			return new CastableString(d == null ? "" : d.toString());
		}
	};

	public static final Converter<String> STRING_OBJ_converter = new Converter<String>()
	{
		@Override public String apply(Castable o)
		{
			return o.asCastableString().getValue();
		}
		@Override public Castable applyInverse(String s)
		{
			return new CastableString(s == null ? "" : s);
		}
	};

	public static Class<?> classOf(Type t)
	{
		if (t instanceof Class<?>) return (Class<?>) t;
		if (t instanceof ParameterizedType) return (Class<?>) ((ParameterizedType) t).getRawType();
		if (t instanceof WildcardType) return (Class<?>) ((WildcardType) t).getUpperBounds()[0];
		throw new RuntimeException();
	}
}
