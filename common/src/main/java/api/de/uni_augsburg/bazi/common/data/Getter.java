package de.uni_augsburg.bazi.common.data;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

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
			return new Getter(name, method.getGenericReturnType(), method);
		}
		return null;
	}

	public static Set<Getter> getters(Class<?> type)
	{
		Set<Getter> getters = new HashSet<>();
		for (Method method : type.getMethods())
		{
			if (!Data.class.isAssignableFrom(method.getDeclaringClass())
				|| method.getDeclaringClass().equals(Data.class)
				|| !Modifier.isPublic(method.getModifiers())) continue;

			Getter getter = asGetter(method);
			if (getter != null) getters.add(getter);
		}
		return getters;
	}


	private final String key;
	private final Type type;
	private final Method method;
	public Getter(String key, Type type, Method method)
	{
		this.key = key;
		this.type = type;
		this.method = method;
	}
	public String key()
	{
		return key;
	}
	public Type type()
	{
		return type;
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
	public String def()
	{
		return method.isAnnotationPresent(Default.class)
			? method.getAnnotation(Default.class).value()
			: null;
	}
}
