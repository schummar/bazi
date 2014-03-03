package de.uni_augsburg.bazi.common.data;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Marco on 03.03.14.
 */
public class Getter
{
	public static Getter asGetter(Method method)
	{
		if (method.getParameterCount() == 0
			&& !method.getReturnType().equals(void.class))
		{
			String name = method.getName();
			name = name.toLowerCase();
			if (name.startsWith("get")) name = name.substring(3);
			return new Getter(method);
		}
		return null;
	}

	public static Set<Getter> getters(Class<?> type)
	{
		Set<Getter> getters = new HashSet<>();
		for (Method method : type.getMethods())
		{
			if (!Data.class.isAssignableFrom(method.getDeclaringClass())
				|| !method.getDeclaringClass().isInterface()
				|| method.getDeclaringClass().equals(Data.class)
				|| !Modifier.isPublic(method.getModifiers())) continue;

			Getter getter = asGetter(method);
			if (getter != null) getters.add(getter);
		}
		return getters;
	}


	private final Method method;
	public Getter(Method method)
	{
		this.method = method;
	}
	public String key()
	{
		return method.getName();
	}
	public Type type()
	{
		return method.getGenericReturnType();
	}
	public Method method()
	{
		return method;
	}
	public Object invoke(Object o)
	{
		try
		{
			method.setAccessible(true); // else cannot access methods defined in lambdas!?
			return method.invoke(o);
		}
		catch (IllegalAccessException | InvocationTargetException e)
		{
			throw new RuntimeException(e);
		}
	}
}
