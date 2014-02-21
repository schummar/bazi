package de.uni_augsburg.bazi.common.format;

/**
* Created by Marco on 21.02.14.
*/
public interface StringConverter<T> extends ObjectConverter<T>
{
	@Override public default String serialize(T value) { return value.toString(); }
	@Override public default T deserialize(Object value) { return deserialize(value.toString()); }
	public T deserialize(String value);
}
