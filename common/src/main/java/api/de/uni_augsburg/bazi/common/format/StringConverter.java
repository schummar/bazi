package de.uni_augsburg.bazi.common.format;

/** {@link ObjectConverter} that serializes/deserializes to/from String. */
public interface StringConverter<T> extends ObjectConverter<T>
{
	@Override default String serialize(T value) { return value.toString(); }

	@Override default T deserialize(Object value) { return deserialize(value.toString()); }

	/**
	 * Deserializes instances from String.
	 * @param value the String to deserialize from.
	 * @return the deserialized object.
	 */
	T deserialize(String value);
}
