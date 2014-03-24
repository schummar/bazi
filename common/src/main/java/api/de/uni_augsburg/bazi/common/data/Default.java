package de.uni_augsburg.bazi.common.data;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to define default values for getters and setters of a
 * {@link Data} interface.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Default
{
	/**
	 * The serialized default value.
	 * That is, of cource, only sensible if the attribute's type can be deserialized from String via
	 * {@link de.uni_augsburg.bazi.common.format.Converters}.
	 * @return the serialized default value.
	 */
	String value();
}
