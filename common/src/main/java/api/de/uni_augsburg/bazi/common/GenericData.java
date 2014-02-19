package de.uni_augsburg.bazi.common;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class GenericData implements InvocationHandler
{
	private final Map<String, Object> data = new HashMap<>();
	private final MList<Class<?>> interfaces = new MList<>();
	private Object proxy;

	public GenericData()
	{
		proxy = new Object();
	}
	public GenericData(Data obj)
	{
		for (Method method : obj.getClass().getMethods())
		{
			String key = asGetter(method);
			if (key != null)
				try
				{
					data.put(key, method.invoke(obj));
				}
				catch (IllegalAccessException | InvocationTargetException e)
				{
					e.printStackTrace();
				}
		}
		cast(Data.class);
	}

	public <T extends Data> T cast(Class<T> type)
	{
		if (!type.isInterface()) throw new RuntimeException("no interface!");

		if (interfaces.contains(type))
		{
			@SuppressWarnings("unchecked") // proxy implements all interfaces in interfaces
				T t = (T) proxy;
			return t;
		}

		interfaces.add(type);
		@SuppressWarnings("unchecked") // proxy implements all interfaces in interfaces
			T t = (T) (
			proxy = Proxy.newProxyInstance(
				GenericData.class.getClassLoader(),
				interfaces.toArray(new Class<?>[interfaces.size()]),
				this
			)
		);
		return t;
	}

	@Override public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
	{
		System.out.println("invoked: " + method.getName());

		if (isCastMethod(method))
			return cast((Class<? extends Data>) args[0]);

		String key = asGetter(method);
		if (key != null)
			return data.get(key);

		key = asSetter(method);
		if (key != null)
		{
			data.put(key, args[0]);
			return null;
		}

		if (method.isDefault())
			System.out.println("default!");

		return null;
	}

	private static boolean isCastMethod(Method method)
	{
		return method.getName().equals("cast")
			&& method.getParameterCount() == 1
			&& method.getParameterTypes()[0].equals(Class.class);
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
}
