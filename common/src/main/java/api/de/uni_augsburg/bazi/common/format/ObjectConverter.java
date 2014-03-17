package de.uni_augsburg.bazi.common.format;

public interface ObjectConverter<T>
{
	Object serialize(T value);
	T deserialize(Object value);
}
