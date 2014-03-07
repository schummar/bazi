package de.uni_augsburg.bazi.common.format;

/**
 * Created by Marco on 21.02.14.
 */
public interface ObjectConverter<T>
{
	Object serialize(T value);
	T deserialize(Object value);
}
