package de.uni_augsburg.bazi.common.format;

import de.uni_augsburg.bazi.common.Resources;

class SimpleConverter<T> implements ObjectConverter<T>
{
	@Override public Object serialize(T value)
	{
		return value;
	}

	@Override public T deserialize(Object value)
	{
		throw new RuntimeException(Resources.get("converter.cannot_deserialize"));
	}
}
