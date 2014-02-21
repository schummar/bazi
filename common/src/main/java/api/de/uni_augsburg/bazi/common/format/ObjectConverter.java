package de.uni_augsburg.bazi.common.format;

/**
* Created by Marco on 21.02.14.
*/
public interface ObjectConverter<T>
{
	public Object serialize(T value);
	public T deserialize(Object value);
}
