package de.uni_augsburg.bazi.common;

import com.google.common.base.Defaults;
import de.uni_augsburg.bazi.common.format.Converters;
import de.uni_augsburg.bazi.common.util.MList;

import java.lang.reflect.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MapData extends LinkedHashMap<String, Object> implements InvocationHandler, Data
{
	public interface ProxyData
	{
		MapData delegate();
	}


	public static MapData fromDataInterface(Data obj)
	{
		if (obj instanceof MapData) return (MapData) obj;
		if (obj instanceof ProxyData) return ((ProxyData) obj).delegate();

		MapData data = new MapData();
		for (Method method : obj.getClass().getMethods())
		{
			if (!Data.class.isAssignableFrom(method.getDeclaringClass())
				|| !method.getDeclaringClass().isInterface()
				|| method.getDeclaringClass().equals(Data.class)
				|| !Modifier.isPublic(method.getModifiers())) continue;

			String key = asGetter(method);
			if (key != null && !data.containsKey(key))
				try
				{
					method.setAccessible(true); // else cannot access methods defined in lambdas!?
					System.out.println(method.getName() + " (" + method.getDeclaringClass() + ") -> " + method.invoke(obj));
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

	public MapData() {}
	public MapData(Map<?, ?> m) { m.forEach((k, v) -> put(k.toString(), v)); }


	@Override public <T extends Data> T cast(Class<? extends T> type)
	{
		if (!type.isInterface()) throw new RuntimeException("no interface!");

		Object proxy = proxies.get(type);
		if (proxy == null)
			proxy = Proxy.newProxyInstance(
				MapData.class.getClassLoader(),
				new Class<?>[]{ProxyData.class, type},
				this
			);

		@SuppressWarnings("unchecked")
		T t = (T) proxy;

		proxies.put(type, t);
		return t;
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
		if (Map.class.isAssignableFrom(type))
		{
			if (value instanceof Data) value = fromDataInterface((Data) value);
			if (value instanceof Map<?, ?>)
			{
				Map<?, Object> map = (Map<?, Object>) value;
				Map<Object, Object> newMap = new LinkedHashMap<>();
				map.forEach((k, v) -> newMap.put(k, cast(v, getGenParam(genType, 1))));
				return newMap;
			}
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
			Map<Object, Object> newMap = new LinkedHashMap<>();
			map.forEach((k, w) -> newMap.put(k, copy(w)));
			return map;
		}
		return v;
	}


	@Override public Map<String, Object> serialize()
	{
		return this;
	}


	@Override public Data merge(Data value)
	{
		MapData asMap = fromDataInterface(value);
		replaceAll((k, v) -> merge(v, asMap.get(k)));
		asMap.forEach(this::putIfAbsent);
		return this;
	}
	private static Object merge(Object v1, Object v2)
	{
		if (v1 == null) return v2;
		if (v2 == null) return v1;


		// Data instance
		if (v1 instanceof Data || v2 instanceof Data)
		{
			if (v1 instanceof Map<?, ?>) v1 = new MapData((Map<?, ?>) v1);
			if (v2 instanceof Map<?, ?>) v2 = new MapData((Map<?, ?>) v2);
			if (v1 instanceof Data && v2 instanceof Data) return ((Data) v1).merge((Data) v2);
			return v2;
		}

		// Array
		else if (v1.getClass().isArray() && v2.getClass().isArray())
		{
			Object[] a1 = (Object[]) v1, a2 = (Object[]) v2;
			Object[] newArray = (Object[]) Array.newInstance(a2.getClass().getComponentType(), Math.max(a1.length, a2.length));
			for (int i = 0; i < newArray.length; i++)
				if (i >= a1.length) newArray[i] = a2[i];
				else if (i >= a2.length) newArray[i] = cast(a1[i], a2.getClass().getComponentType());
				else newArray[i] = merge(a1[i], a2[i]);
			return newArray;
		}

		// List
		if (v1 instanceof List<?> && v2 instanceof List<?>)
		{
			List<?> l1 = (List<?>) v1, l2 = (List<?>) v2;
			List<Object> newList = new MList<>();
			for (int i = 0; i < Math.max(l1.size(), l2.size()); i++)
				if (i >= l1.size()) newList.add(l2.get(i));
				else if (i >= l2.size()) newList.add(l1.get(i));
				else newList.add(merge(l1.get(i), l2.get(i)));
			return newList;
		}

		// Map
		if (v1 instanceof Map<?, ?> && v2 instanceof Map<?, ?>)
		{
			Map<?, ?> m1 = (Map<?, ?>) v1, m2 = (Map<?, ?>) v2;
			Map<Object, Object> newMap = new LinkedHashMap<>(m2);
			newMap.replaceAll((k, v) -> merge(m1.get(k), v));
			m1.forEach(newMap::putIfAbsent);
			return newMap;
		}

		return v2;
	}


	@SuppressWarnings("unchecked")
	@Override public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
	{
		if (overriddenBy(method, getClass().getMethod("cast", Class.class)))
			return cast((Class<? extends Data>) args[0]);

		if (overriddenBy(method, getClass().getMethod("merge", Data.class)))
			return merge((Data) args[0]);

		if (overriddenBy(method, getClass().getMethod("copy")))
			return copy();

		if (overriddenBy(method, getClass().getMethod("serialize")))
			return serialize();

		if (overriddenBy(method, Object.class.getMethod("toString")))
			return toString();

		if (overriddenBy(method, ProxyData.class.getMethod("delegate")))
			return this;

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
