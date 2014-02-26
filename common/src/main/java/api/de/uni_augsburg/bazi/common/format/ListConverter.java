package de.uni_augsburg.bazi.common.format;

import java.util.List;

/**
 * Created by Marco on 26.02.14.
 */
public interface ListConverter<T> extends ObjectConverter<T>
{
	@Override List<?> serialize(T value);
	T deserialize(List<?> list);

	@Override default T deserialize(Object value)
	{
		if (!(value instanceof List)) throw new RuntimeException("no list");
		return deserialize((List<?>) value);
	}
}
