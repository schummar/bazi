package de.schummar.castable;

import javafx.beans.property.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.*;
import java.util.*;

public abstract class Converters
{
	private static final Map<Method, Converter> METHOD_CONVERTER_CACHE = new HashMap<>();
	public static Converter get(Method method)
	{
		Converter converter = METHOD_CONVERTER_CACHE.get(method);
		if (converter == null)
		{
			try
			{
				converter = _get(method);
			}
			catch (Exception e) { throw new RuntimeException(e); }
			METHOD_CONVERTER_CACHE.put(method, converter);
		}
		return converter;
	}
	private static Converter _get(Method method) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException
	{
		Attribute attribute = method.getAnnotation(Attribute.class);
		if (attribute.converter() != Converter.class) return attribute.converter().getConstructor().newInstance();

		Type type = method.getReturnType().isAssignableFrom(Property.class)
			? ((ParameterizedType) method.getGenericReturnType()).getActualTypeArguments()[0]
			: method.getGenericReturnType();
		Converter contentconverter = null;
		if (attribute.contentConverter() != Converter.class) contentconverter = attribute.contentConverter().getConstructor().newInstance();
		return get(type, contentconverter);
	}


	private static final Map<Type, Converter<?>> TYPE_CONVERTER_CACHE = new HashMap<>();
	public static Converter<?> get(Type type, Converter<?> contentConverter)
	{
		Converter<?> converter = null;
		if (contentConverter == null) converter = TYPE_CONVERTER_CACHE.get(type);
		if (converter == null)
		{
			try
			{
				converter = _get(type, contentConverter);
			}
			catch (Exception e) { throw new RuntimeException(e); }
			if (contentConverter == null) TYPE_CONVERTER_CACHE.put(type, converter);
		}
		return converter;
	}
	private static Converter<?> _get(Type type, Converter<?> contentConverter) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException
	{
		Class<?> c = classOf(type);

		if (c.isAnnotationPresent(Convert.class))
			return c.getAnnotation(Convert.class).value().getConstructor().newInstance();

		Queue<Class<?>> q = new LinkedList<>();
		q.add(c);
		while (!q.isEmpty())
		{
			Class<?> cur = q.remove();
			if (cur.isAnnotationPresent(Convert.class) && cur.getAnnotation(Convert.class).forSubClasses())
			{
				try {return cur.getAnnotation(Convert.class).value().getConstructor(Type.class).newInstance(type);}
				catch (Exception ignore) {}
				try {return cur.getAnnotation(Convert.class).value().getConstructor(Class.class).newInstance(c);}
				catch (Exception ignore) {}
				try { return cur.getAnnotation(Convert.class).value().getConstructor().newInstance();}
				catch (Exception e) { throw new RuntimeException(e); }
			}


			Class<?> sup = cur.getSuperclass();
			if (sup != null) q.add(sup);
			q.addAll(Arrays.asList(cur.getInterfaces()));
		}

		if (c == Boolean.class || c == boolean.class) return BOOLEAN_OBJ_CONVERTER;
		if (c == Integer.class || c == int.class) return INTEGER_OBJ_CONVERTER;
		if (c == Long.class || c == long.class) return LONG_OBJ_CONVERTER;
		if (c == Float.class || c == float.class) return FLOAT_OBJ_CONVERTER;
		if (c == Double.class || c == double.class) return DOUBLE_OBJ_CONVERTER;
		if (c == String.class) return STRING_OBJ_CONVERTER;
		if (Enum.class.isAssignableFrom(c)) return EnumConverter.of(c);

		if (c.isAssignableFrom(DataList.class))
		{
			Type contentType = ((ParameterizedType) type).getActualTypeArguments()[0];
			if (contentConverter == null)
				contentConverter = get(contentType, null);

			return c.isAssignableFrom(CList.class)
				? new ListConverter<>(contentConverter)
				: create(contentConverter, classOf(contentType));
		}

		if (c.isAssignableFrom(CMap.class))
		{
			Type keyType = ((ParameterizedType) type).getActualTypeArguments()[0];
			if (keyType != String.class)
				throw new RuntimeException(String.format("Cannot convert map with key type other than String (used %s).", keyType));
			Type contentType = ((ParameterizedType) type).getActualTypeArguments()[1];
			if (contentConverter == null)
				contentConverter = get(contentType, null);

			return new MapConverter<>(contentConverter);
		}

		if (Data.class.isAssignableFrom(c) && c.isInterface())
		{
			@SuppressWarnings("unchecked")
			Class<? extends Data> d = (Class<? extends Data>) c;
			return new ObjectConverter<>(d);
		}

		throw new RuntimeException(String.format("No converter for type %s available.", type));
	}


	public static class ListConverter<T> implements Converter<CList<T>>
	{
		private final Converter<T> contentConverter;
		public ListConverter(Converter<T> contentConverter)
		{
			this.contentConverter = contentConverter;
		}
		@Override public CList<T> apply(Castable o)
		{
			return new CList<>(o.asCastableList(), contentConverter);
		}
		@Override public Castable applyInverse(CList<T> ts)
		{
			CastableList o = new CastableList();
			if (ts != null && !ts.isEmpty())
				new CList<>(o, contentConverter).addAll(ts);
			return o;
		}
	}

	public static <T extends Data> DataListConverter<T> create(Converter<?> converter, Class<?> type)
	{
		@SuppressWarnings("unchecked") Converter<T> tConverter = (Converter<T>) converter;
		@SuppressWarnings("unchecked") Class<T> tType = (Class<T>) type;
		return new DataListConverter<>(tConverter, tType);
	}
	public static class DataListConverter<T extends Data> implements Converter<DataList<T>>
	{
		private final Converter<T> contentConverter;
		private final Class<T> contentType;
		public DataListConverter(Converter<T> contentConverter, Class<T> contentType)
		{
			this.contentConverter = contentConverter;
			this.contentType = contentType;
		}
		@Override public DataList<T> apply(Castable o)
		{
			return new DataList<>(o.asCastableList(), contentConverter, contentType);
		}
		@Override public Castable applyInverse(DataList<T> ts)
		{
			CastableList o = new CastableList();
			if (ts != null && !ts.isEmpty())
				new DataList<>(o, contentConverter, contentType).addAll(ts);
			return o;
		}
	}

	public static class MapConverter<T> implements Converter<CMap<T>>
	{
		private final Converter<T> contentConverter;
		public MapConverter(Converter<T> contentConverter)
		{
			this.contentConverter = contentConverter;
		}
		@Override public CMap<T> apply(Castable o)
		{
			return new CMap<>(o.asCastableObject(), contentConverter);
		}
		@Override public Castable applyInverse(CMap<T> tcMap)
		{
			CastableObject o = new CastableObject();
			if (tcMap != null && !tcMap.isEmpty())
				new CMap<>(o, contentConverter).putAll(tcMap);
			return o;
		}
	}


	public static class ObjectConverter<T extends Data> implements Converter<T>
	{
		private final Class<T> c;
		public ObjectConverter(Class<T> c)
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
			if (t == null) return new CastableString();
			if (t instanceof ConvertibleEnum)
				return new CastableString(((ConvertibleEnum) t).key());
			return new CastableString(t.name());
		}
	}

	public static final Converter<Boolean> BOOLEAN_OBJ_CONVERTER = new Converter<Boolean>()
	{
		@Override public Boolean apply(Castable o)
		{
			try
			{
				String s = o.asCastableString().getValue();
				return Boolean.parseBoolean(s);
			}
			catch (Exception ignore) {}
			return false;
		}
		@Override public Castable applyInverse(Boolean v)
		{
			return new CastableString(v == null ? "" : v.toString());
		}
	};

	public static final Converter<Integer> INTEGER_OBJ_CONVERTER = new Converter<Integer>()
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

	public static final Converter<Long> LONG_OBJ_CONVERTER = new Converter<Long>()
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

	public static final Converter<Float> FLOAT_OBJ_CONVERTER = new Converter<Float>()
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

	public static final Converter<Double> DOUBLE_OBJ_CONVERTER = new Converter<Double>()
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

	public static final Converter<String> STRING_OBJ_CONVERTER = new Converter<String>()
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
