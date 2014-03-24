package de.uni_augsburg.bazi.common.data;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

class Setter
{
	public static Setter asSetter(Method method)
	{
		if (method.getParameterCount() == 1
			&& method.getReturnType().equals(void.class))
		{
			String name = method.getName();
			name = name.toLowerCase();
			if (name.startsWith("set")) name = name.substring(3);
			return new Setter(name, method.getGenericReturnType());
		}
		return null;
	}

	public static Set<Setter> setters(Class<?> type)
	{
		Set<Setter> setters = new HashSet<>();
		for (Method method : type.getMethods())
		{
			if (!Data.class.isAssignableFrom(method.getDeclaringClass())
				|| !method.getDeclaringClass().isInterface()
				|| method.getDeclaringClass().equals(Data.class)
				|| !Modifier.isPublic(method.getModifiers())) continue;

			Setter setter = asSetter(method);
			if (setter != null) setters.add(setter);
		}
		return setters;
	}


	private final String key;
	private final Type type;
	public Setter(String key, Type type)
	{
		this.key = key;
		this.type = type;
	}
	public String key()
	{
		return key;
	}
	public Type type()
	{
		return type;
	}
}
