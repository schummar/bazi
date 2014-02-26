package de.uni_augsburg.bazi.common;

import com.google.common.base.Defaults;
import com.google.common.collect.ForwardingMap;
import de.uni_augsburg.bazi.common.format.Converters;

import java.lang.reflect.*;
import java.util.*;

public class MapData extends ForwardingMap<String, Object> implements InvocationHandler, Data
{
	public static MapData fromDataInterface(Data obj)
	{
		MapData data = new MapData();
		for (Method method : obj.getClass().getMethods())
		{
			if (!Data.class.isAssignableFrom(method.getDeclaringClass())
				|| method.getDeclaringClass().equals(Data.class)) continue;

			String key = asGetter(method);
			if (key != null && !data.containsKey(key))
				try
				{
					data.put(key, method.invoke(obj));
				}
				catch (IllegalAccessException | InvocationTargetException e)
				{
					e.printStackTrace();
				}
		}

		if (obj.getClass().isInterface())
			data.cast(obj.getClass());
		else for (Class<?> i : obj.getClass().getInterfaces())
			if (Data.class.isAssignableFrom(i))
			{
				@SuppressWarnings("unchecked")
				Class<? extends Data> dataType = (Class<? extends Data>) i;
				data.cast(dataType);
			}

		return data;
	}


	private final Map<Class<?>, Data> proxies = new HashMap<>();
	private boolean mutable = true;
	Map<String, Object> delegateMap = new HashMap<>();

	public MapData() {}
	public MapData(Map<?, ?> m) { m.forEach((k, v) -> put(k.toString(), v)); }

	@Override protected Map<String, Object> delegate()
	{
		return delegateMap;
	}
	@Override public Object put(String key, Object value)
	{
		System.out.println(key + " -> " + value);
		if (value instanceof Data) value = ((Data) value).asMap();
		return super.put(key, value);
	}
	@Override public void putAll(Map<? extends String, ?> map)
	{
		map.forEach(this::put);
	}


	@Override public boolean isMutable()
	{
		return mutable;
	}

	@Override public <T extends Data> T cast(Class<T> type)
	{
		if (!type.isInterface()) throw new RuntimeException("no interface!");

		Object proxy = proxies.get(type);
		if (proxy == null)
			proxy = Proxy.newProxyInstance(
				MapData.class.getClassLoader(),
				new Class<?>[]{type},
				this
			);

		@SuppressWarnings("unchecked")
		T t = (T) proxy;

		proxies.put(type, t);
		return t;
	}

	@Override public MapData copy()
	{
		return new MapData(this);
	}
	@Override public MapData immutable()
	{
		mutable = false;
		return this;
	}
	@Override public MapData asMap()
	{
		return this;
	}


	@SuppressWarnings("unchecked")
	public Object cast(Object value, Type genType)
	{
		Class<?> type;
		Type gen = null;
		if (genType instanceof Class)
		{
			type = (Class<?>) genType;
		}
		else
		{
			type = (Class<?>) ((ParameterizedType) genType).getRawType();
			gen = ((ParameterizedType) genType).getActualTypeArguments()[0];
		}

		// already correct type
		if (type.isInstance(value)) return type.cast(value);

		// Data instance
		if (Data.class.isAssignableFrom(type))
		{
			if (value instanceof Data)
				return (T) ((Data) value).cast((Class<? extends Data>) type);
			if (value instanceof Map<?, ?>)
				return (T) new MapData((Map<?, ?>) value).cast((Class<? extends Data>) type);
		}

		// Array
		// List
		// Map

		value = Converters.deserialize(value, type);
		if (type.isInstance(value)) return type.cast(value);

		throw new IncompatibleTypesException(String.format("cannot cast %s to %s", value.getClass(), type));
	}


	@SuppressWarnings("unchecked")
	@Override public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
	{
		if (overriddenBy(method, getClass().getMethod("cast", Class.class)))
			return cast((Class<? extends Data>) args[0]);

		if (overriddenBy(method, getClass().getMethod("copy")))
			return copy();

		if (overriddenBy(method, getClass().getMethod("immutable")))
			return immutable();

		if (overriddenBy(method, getClass().getMethod("isMutable")))
			return isMutable();

		if (overriddenBy(method, getClass().getMethod("asMap")))
			return asMap();

		if (overriddenBy(method, Object.class.getMethod("toString")))
			return toString();

		String key = asGetter(method);
		if (key != null)
		{
			if (!containsKey(key)) return Defaults.defaultValue(method.getReturnType());
			return cast(get(key), method.getGenericReturnType());
		}

		key = asSetter(method);
		if (key != null)
		{
			put(key, args[0]);
			return null;
		}

		return Defaults.defaultValue(method.getReturnType());
	}


	private static boolean overriddenBy(Method m0, Method m1)
	{
		if (!m0.getName().equals(m1.getName())
			|| !m0.getReturnType().isAssignableFrom(m1.getReturnType())
			|| m0.getParameterCount() != m1.getParameterCount())
			return false;

		for (int i = 0; i < m0.getParameterCount(); i++)
			if (!m1.getParameterTypes()[i].isAssignableFrom(m0.getParameterTypes()[i]))
				return false;
		return true;
	}


	private static String asGetter(Method method)
	{
		if (method.getParameterCount() == 0
			&& !method.getReturnType().equals(void.class))
		{
			String name = method.getName();
			name = name.toLowerCase();
			if (name.startsWith("get")) name = name.substring(3);
			return name;
		}
		return null;
	}


	private static String asSetter(Method method)
	{
		if (method.getParameterCount() == 1
			&& method.getReturnType().equals(void.class))
		{
			String name = method.getName();
			name = name.toLowerCase();
			if (name.startsWith("set")) name = name.substring(3);
			return name;
		}
		return null;
	}


	public class IncompatibleTypesException extends RuntimeException
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
