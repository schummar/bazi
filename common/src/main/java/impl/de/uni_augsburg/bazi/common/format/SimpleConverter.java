package de.uni_augsburg.bazi.common.format;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

class SimpleConverter<T> implements ObjectConverter<T>
{
	@Override public Object serialize(T value)
	{
		Map<String, Object> map = new HashMap<>();
		for (Field field : value.getClass().getFields())
		{
			System.out.println(field);
			field.setAccessible(true);
			try
			{
				map.put(field.getName(), Converters.serialize(field.get(value)));
			}
			catch (IllegalAccessException ignored)
			{
				ignored.printStackTrace();
			}
		}

		return map;
	}
}
