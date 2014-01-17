package de.uni_augsburg.bazi.common;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.TreeMap;

import com.google.common.primitives.Primitives;

public interface toJsonMixin
{
	public static Object toPOJO(Object o)
	{
		Map<String, Object> data = new TreeMap<>();
		for (Method method : o.getClass().getDeclaredMethods())
		{
			if (Primitives.wrap(method.getReturnType()).equals(Void.class)
					|| method.getParameterCount() > 0)
				continue;
			try
			{
				data.put(method.getName(), method.invoke(o));
			}
			catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
			{
				e.printStackTrace();
			}
		}
		return data;
	}

	public static String toJson(Object o)
	{
		return Json.toJson(o);
	}

	public default String toJson()
	{
		return toJson(toPOJO(this));
	}
}
