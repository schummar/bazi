package de.schummar.castable;

import com.google.common.primitives.Primitives;
import javafx.beans.property.Property;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.List;

public abstract class Converters
{
	public static Converter<?> get(Type type, Converter<?> contentAdapter)
	{
		Class<?> c = classOf(type);
		c = Primitives.wrap(c);

		if (c == Integer.class) return INTEGER_OBJ_ADAPTER;
		if (c == Long.class) return LONG_OBJ_ADAPTER;
		if (c == Float.class) return FLOAT_OBJ_ADAPTER;
		if (c == Double.class) return DOUBLE_OBJ_ADAPTER;
		if (c == String.class) return STRING_OBJ_ADAPTER;

		if (c.isAssignableFrom(List.class))
		{
			if (contentAdapter == null)
				contentAdapter = get(((ParameterizedType) type).getActualTypeArguments()[0], null);
			return new ListAdapter<>(contentAdapter);
		}

		if (c.isInterface())
		{
			return new MapAdapter<>(c);
		}

		throw new RuntimeException(String.format("No adapter for type %s available.", type));
	}

	public static class ListAdapter<T> implements Converter<List<T>>
	{
		private final Converter<T> adapter;
		public ListAdapter(Converter<T> adapter)
		{
			this.adapter = adapter;
		}
		@Override public List<T> apply(Castable o)
		{
			return o.asCastableList().cast(adapter);
		}
		@Override public Castable applyInverse(List<T> ts)
		{
			CastableList o = new CastableList();
			if (ts != null && !ts.isEmpty())
				o.cast(adapter).addAll(ts);
			return o;
		}
	}

	public static class MapAdapter<T> implements Converter<T>
	{
		private final Class<T> c;
		public MapAdapter(Class<T> c)
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

	public static final Converter<Integer> INTEGER_OBJ_ADAPTER = new Converter<Integer>()
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

	public static final Converter<Long> LONG_OBJ_ADAPTER = new Converter<Long>()
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

	public static final Converter<Float> FLOAT_OBJ_ADAPTER = new Converter<Float>()
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

	public static final Converter<Double> DOUBLE_OBJ_ADAPTER = new Converter<Double>()
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

	public static final Converter<String> STRING_OBJ_ADAPTER = new Converter<String>()
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
