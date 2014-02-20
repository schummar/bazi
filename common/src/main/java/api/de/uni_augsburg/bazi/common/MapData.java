package de.uni_augsburg.bazi.common;

import com.google.common.base.Defaults;
import com.google.common.collect.ForwardingMap;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
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
	public MapData(Map<? extends String, ?> m) { putAll(m); }

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
	public <T> T get(String key, Class<T> type)
	{
		if (!containsKey(key)) return Defaults.defaultValue(type);

		Object value = get(key);

		if (type.isInstance(value)) return type.cast(value);

		if (Data.class.isInstance(value)
			&& Data.class.isAssignableFrom(type))
		{
			Data data = (Data) value;
			Class<? extends Data> dataType = (Class<? extends Data>) type;
			return (T) data.cast(dataType);
		}

		throw new IncompatibleTypesException(String.format("cannot cast %s to %s", value.getClass(), type));
	}


	@SuppressWarnings("unchecked")
	@Override public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
	{
		if (equals(method, getClass().getMethod("cast", Class.class)))
			return cast((Class<? extends Data>) args[0]);

		if (equals(method, getClass().getMethod("copy")))
			return copy();

		if (equals(method, getClass().getMethod("immutable")))
			return immutable();

		if (equals(method, getClass().getMethod("isMutable")))
			return isMutable();

		if (equals(method, getClass().getMethod("asMap")))
			return asMap();

		if (equals(method, Object.class.getMethod("toString")))
			return toString();

		String key = asGetter(method);
		if (key != null)
		{
			return get(key, method.getReturnType());
		}

		key = asSetter(method);
		if (key != null)
		{
			put(key, args[0]);
			return null;
		}

		return Defaults.defaultValue(method.getReturnType());
	}


	private static boolean equals(Method m0, Method m1)
	{
		return m0.getName().equals(m1.getName())
			&& m0.getReturnType().equals(m1.getReturnType())
			&& Arrays.equals(m0.getParameterTypes(), m1.getParameterTypes());
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
