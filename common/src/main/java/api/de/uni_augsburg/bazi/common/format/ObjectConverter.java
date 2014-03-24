package de.uni_augsburg.bazi.common.format;

import de.uni_augsburg.bazi.common.Resources;

/**
 * An ObjectConverter serializes/deserializes instances to/from raw data as specified in {@link Format}.
 * @param <T> the type to be serialized/deserialized.
 * @see Format
 */
public interface ObjectConverter<T>
{
	/**
	 * Serializes instances to raw data as specified in {@link Format}.
	 * @param value the object to serialize.
	 * @return raw data
	 * @see Format
	 */
	Object serialize(T value);

	/**
	 * deserializes instances from raw data as specified in {@link Format}.
	 * @param value the raw data to deserialize from.
	 * @return the deserialized object.
	 * @see Format
	 */
	default T deserialize(Object value)
	{
		throw new RuntimeException(Resources.get("converter.cannot_deserialize"));
	}
}
