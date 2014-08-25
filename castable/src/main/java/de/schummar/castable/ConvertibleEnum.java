package de.schummar.castable;

/** Should only be implemented by Enum types. */
public interface ConvertibleEnum
{
	/**
	 * Unique key of this value.
	 * (key = serialized value)
	 * @return the unique key of this value.
	 */
	String key();

	/**
	 * Whether this value matches that key.
	 * (key = serialized value)
	 * @param s the key.
	 * @return true iff this value matches that key.
	 */
	boolean matches(String s);
}
