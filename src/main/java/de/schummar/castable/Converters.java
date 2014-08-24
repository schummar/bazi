package de.schummar.castable;

import javafx.beans.property.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.*;
import java.util.*;

public abstract class Converters
{
	private static final Map<Method, Converter<?>> METHOD_CONVERTER_CACHE = new HashMap<>();
	public static <T> Converter<T> get(Method method)
	{
		Converter<?> converter = METHOD_CONVERTER_CACHE.get(method);
		if (converter == null)
		{
			try
			{
				converter = _get(method);
			}
			catch (Exception e) { throw new RuntimeException(e); }
			METHOD_CONVERTER_CACHE.put(method, converter);
		}
		@SuppressWarnings("unchecked")
		Converter<T> tConverter = (Converter<T>) converter;
		return tConverter;
	}
	private static Converter _get(Method method) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException
	{
		Type type = method.getReturnType().isAssignableFrom(CProperty.class)
			? ((ParameterizedType) method.getGenericReturnType()).getActualTypeArguments()[0]
			: method.getGenericReturnType();

		Attribute attribute = method.getAnnotation(Attribute.class);
		if (attribute.converter() != Converter.class) return createConverter(attribute.converter(), type);

		Converter contentconverter = null;
		if (attribute.contentConverter() != Converter.class)
		{
			Type contentType = ((ParameterizedType) type).getActualTypeArguments()[0];
			contentconverter = createConverter(attribute.contentConverter(), contentType);
		}
		return get(type, contentconverter);
	}


	private static final Map<Type, Converter<?>> TYPE_CONVERTER_CACHE = new HashMap<>();
	public static <T> Converter<T> get(Class<T> c)
	{
		@SuppressWarnings("unchecked")
		Converter<T> converter = (Converter<T>) get(c, null);
		return converter;
	}
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
			return createConverter(c.getAnnotation(Convert.class).value(), type);

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
				: createListConverter(contentConverter, classOf(contentType));
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

		Queue<Class<?>> q = new LinkedList<>();
		q.add(c);
		while (!q.isEmpty())
		{
			Class<?> cur = q.remove();
			if (cur.isAnnotationPresent(Convert.class) && cur.getAnnotation(Convert.class).forSubClasses())
				return createConverter(cur.getAnnotation(Convert.class).value(), type);


			Class<?> sup = cur.getSuperclass();
			if (sup != null) q.add(sup);
			q.addAll(Arrays.asList(cur.getInterfaces()));
		}

		throw new RuntimeException(String.format("No converter for type %s available.", type));
	}
	private static Converter<?> createConverter(Class<? extends Converter> cClass, Type type)
	{
		try {return cClass.getConstructor(Type.class).newInstance(type);}
		catch (Exception ignore) {}
		try {return cClass.getConstructor(Class.class).newInstance(classOf(type));}
		catch (Exception ignore) {}
		try { return cClass.getConstructor().newInstance();}
		catch (Exception e) { throw new RuntimeException(e); }
	}


	@ConvertShallow public static class ListConverter<T> implements Converter<CList<T>>
	{
		private final Converter<T> contentConverter;
		public ListConverter(Converter<T> contentConverter)
		{
			this.contentConverter = contentConverter;
		}
		@Override public CList<T> unpack(Castable c)
		{
			return new CList<>(c.asCastableList(), contentConverter);
		}
		@Override public Castable pack(CList<T> ts)
		{
			CastableList l = new CastableList();
			if (ts != null && !ts.isEmpty())
				new CList<>(l, contentConverter).addAll(ts);
			return l;
		}
	}

	public static <T extends Data> DataListConverter<T> createListConverter(Converter<?> converter, Class<?> type)
	{
		@SuppressWarnings("unchecked") Converter<T> tConverter = (Converter<T>) converter;
		@SuppressWarnings("unchecked") Class<T> tType = (Class<T>) type;
		return new DataListConverter<>(tConverter, tType);
	}
	@ConvertShallow public static class DataListConverter<T extends Data> implements Converter<DataList<T>>
	{
		private final Converter<T> contentConverter;
		private final Class<T> contentType;
		public DataListConverter(Converter<T> contentConverter, Class<T> contentType)
		{
			this.contentConverter = contentConverter;
			this.contentType = contentType;
		}
		@Override public DataList<T> unpack(Castable c)
		{
			return new DataList<>(c.asCastableList(), contentConverter, contentType);
		}
		@Override public Castable pack(DataList<T> ts)
		{
			CastableList l = new CastableList();
			if (ts != null && !ts.isEmpty())
				new DataList<>(l, contentConverter, contentType).addAll(ts);
			return l;
		}
	}

	@ConvertShallow public static class MapConverter<T> implements Converter<CMap<T>>
	{
		private final Converter<T> contentConverter;
		public MapConverter(Converter<T> contentConverter)
		{
			this.contentConverter = contentConverter;
		}
		@Override public CMap<T> unpack(Castable o)
		{
			return new CMap<>(o.asCastableObject(), contentConverter);
		}
		@Override public Castable pack(CMap<T> tcMap)
		{
			CastableObject o = new CastableObject();
			if (tcMap != null && !tcMap.isEmpty())
				new CMap<>(o, contentConverter).putAll(tcMap);
			return o;
		}
	}


	@ConvertShallow public static class ObjectConverter<T extends Data> implements Converter<T>
	{
		private final Class<T> type;
		public ObjectConverter(Class<T> type)
		{
			this.type = type;
		}
		@Override public T unpack(Castable o)
		{
			return o.asCastableObject().cast(type);
		}
		@Override public Castable pack(T t)
		{
			CastableObject o = new CastableObject();
			if (t != null) for (Method method : t.getClass().getMethods())
			{
				try { method = type.getMethod(method.getName(), method.getParameterTypes()); } // no annotations on proxy
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

		@Override public T unpack(Castable o)
		{
			String s = o.asCastableString().getValue();
			if (s == null) return null;
			if (!ConvertibleEnum.class.isAssignableFrom(type))
				return Enum.valueOf(type, s);

			String name = s.toLowerCase();
			for (T t : type.getEnumConstants())
				if (((ConvertibleEnum) t).matches(name))
					return t;

			LOGGER.error(String.format("No enum value in %s matches '%s'.", type, o));
			return type.getEnumConstants()[0];
		}
		@Override public Castable pack(T t)
		{
			CastableString s = new CastableString();
			if (t instanceof ConvertibleEnum)
				s.setValue(((ConvertibleEnum) t).key());
			else if (t != null) s.setValue(t.name());
			return s;
		}
	}

	public static final Converter<Boolean> BOOLEAN_OBJ_CONVERTER = new Converter<Boolean>()
	{
		@Override public Boolean unpack(Castable o)
		{
			String s = o.asCastableString().getValue();
			return s == null ? null : Boolean.parseBoolean(s);
		}
		@Override public Castable pack(Boolean v)
		{
			return new CastableString(v == null ? null : v.toString());
		}
	};

	public static final Converter<Integer> INTEGER_OBJ_CONVERTER = new Converter<Integer>()
	{
		@Override public Integer unpack(Castable o)
		{
			String s = o.asCastableString().getValue();
			return s == null ? null : Integer.parseInt(s);
		}
		@Override public Castable pack(Integer v)
		{
			return new CastableString(v == null ? null : v.toString());
		}
	};

	public static final Converter<Long> LONG_OBJ_CONVERTER = new Converter<Long>()
	{
		@Override public Long unpack(Castable o)
		{
			String s = o.asCastableString().getValue();
			return s == null ? null : Long.parseLong(s);
		}
		@Override public Castable pack(Long v)
		{
			return new CastableString(v == null ? null : v.toString());
		}
	};

	public static final Converter<Float> FLOAT_OBJ_CONVERTER = new Converter<Float>()
	{
		@Override public Float unpack(Castable o)
		{
			String s = o.asCastableString().getValue();
			return s == null ? null : Float.parseFloat(s);
		}
		@Override public Castable pack(Float v)
		{
			return new CastableString(v == null ? null : v.toString());
		}
	};

	public static final Converter<Double> DOUBLE_OBJ_CONVERTER = new Converter<Double>()
	{
		@Override public Double unpack(Castable o)
		{
			String s = o.asCastableString().getValue();
			return s == null ? null : Double.parseDouble(s);
		}
		@Override public Castable pack(Double v)
		{
			return new CastableString(v == null ? null : v.toString());
		}
	};

	public static final Converter<String> STRING_OBJ_CONVERTER = new Converter<String>()
	{
		@Override public String unpack(Castable o)
		{
			return o.asCastableString().getValue();
		}
		@Override public Castable pack(String v)
		{
			return new CastableString(v);
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
