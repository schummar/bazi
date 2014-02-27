package de.uni_augsburg.bazi.common;

import com.google.common.base.Defaults;
import de.uni_augsburg.bazi.common.format.Converters;
import de.uni_augsburg.bazi.common.util.MList;

import java.lang.reflect.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MapData extends HashMap<String, Object> implements InvocationHandler, Data
{
	public static MapData fromDataInterface(Data obj)
	{
		MapData data = new MapData();
		for (Method method : obj.getClass().getMethods())
		{
			if (!Data.class.isAssignableFrom(method.getDeclaringClass())
				|| method.getDeclaringClass().equals(Data.class)
				|| !Modifier.isPublic(method.getModifiers())) continue;

			String key = asGetter(method);
			if (key != null && !data.containsKey(key))
				try
				{
					method.setAccessible(true); // else cannot access methods defined in lambdas!?
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


	@Override public boolean isMutable() { return mutable; }

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
		MapData copy = new MapData();
		forEach((k, v) -> copy.put(k, copy(v)));
		return copy;
	}

	@SuppressWarnings("unchecked")
	private static Object copy(Object v)
	{
		if (v instanceof Data) return ((Data) v).copy();
		if (v.getClass().isArray())
		{
			Object[] array = (Object[]) v;
			Object[] newArray = (Object[]) Array.newInstance(v.getClass().getComponentType(), array.length);
			for (int i = 0; i < array.length; i++)
				newArray[i] = copy(array[i]);
			return newArray;
		}
		if (v instanceof List<?>)
		{
			return ((List) v).stream().map(MapData::copy).collect(MList.collector());
		}
		if (v instanceof Map<?, ?>)
		{
			Map<?, ?> map = (Map<?, ?>) v;
			Map<Object, Object> newMap = new HashMap<>();
			map.forEach((k, w) -> newMap.put(k, copy(w)));
			return map;
		}
		return v;
	}


	@Override public MapData immutable()
	{
		mutable = false;
		return this;
	}


	@Override public Map<String, Object> serialize()
	{
		return this;
	}


	@SuppressWarnings("unchecked")
	public static Object cast(Object value, Type genType)
	{
		Class<?> type = getRaw(genType);

		// Data instance
		if (Data.class.isAssignableFrom(type))
		{
			if (type.isInstance(value)) return value;
			if (value instanceof Data)
				return ((Data) value).cast((Class<? extends Data>) type);
			if (value instanceof Map<?, ?>)
				return new MapData((Map<?, ?>) value).cast((Class<? extends Data>) type);
		}

		// Array
		else if (type.isArray() && value.getClass().isArray())
		{
			if (type.isInstance(value) && type.getComponentType().equals(value.getClass().getComponentType())) return value;
			Object[] array = (Object[]) value;
			Object[] newArray = (Object[]) Array.newInstance(array.getClass().getComponentType(), array.length);
			for (int i = 0; i < array.length; i++)
				newArray[i] = cast(array[i], array.getClass().getComponentType());
			return newArray;
		}

		// List
		if (List.class.isAssignableFrom(type) && value instanceof List<?>)
		{
			List<Object> list = (List<Object>) value;
			List<Object> newList = new MList<>();
			list.forEach(o -> newList.add(cast(o, getGenParam(genType, 0))));
			return newList;
		}

		// Map
		if (Map.class.isAssignableFrom(type) && value instanceof Map<?, ?>)
		{
			Map<?, Object> map = (Map<?, Object>) value;
			Map<Object, Object> newMap = new HashMap<>();
			map.forEach((k, v) -> newMap.put(k, cast(v, getGenParam(genType, 1))));
			return newMap;
		}

		if (type.isInstance(value)) return value;
		try
		{
			return Converters.deserialize(value, type);
		}
		catch (Exception e)
		{
			throw new IncompatibleTypesException(String.format("cannot cast %s to %s", value.getClass(), type));
		}
	}

	private static Class<?> getRaw(Type type)
	{
		if (type instanceof Class<?>) return (Class<?>) type;
		if (type instanceof ParameterizedType) return (Class<?>) ((ParameterizedType) type).getRawType();
		if (type instanceof WildcardType && ((WildcardType) type).getUpperBounds().length > 0)
			return (Class<?>) ((WildcardType) type).getUpperBounds()[0];
		return Object.class;
	}

	private static Type getGenParam(Type type, int index)
	{
		return ((ParameterizedType) type).getActualTypeArguments()[index];
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

		if (overriddenBy(method, getClass().getMethod("serialize")))
			return serialize();

		if (overriddenBy(method, Object.class.getMethod("toString")))
			return toString();

		String key = asGetter(method);
		if (key != null)
		{
			if (!containsKey(key)) return Defaults.defaultValue(method.getReturnType());
			Object value = cast(get(key), method.getGenericReturnType());
			put(key, value);
			return value;
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


	@Override public String toString()
	{
		if (values().stream().anyMatch(
			v ->
				v instanceof Data || v instanceof List || v instanceof Map
		))
			return "{\n  " + entrySet().stream().map(Object::toString).collect(Collectors.joining(",\n")).replaceAll("\n", "\n  ") + "\n}";
		return super.toString();
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
