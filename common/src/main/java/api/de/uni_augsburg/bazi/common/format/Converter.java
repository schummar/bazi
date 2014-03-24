package de.uni_augsburg.bazi.common.format;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to point to an {@link ObjectConverter}-class that serializes / deserializes
 * instances of the annotated class.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Converter
{
	/**
	 * The {@link ObjectConverter}-class.
	 * @return the {@link ObjectConverter}-class.
	 */
	Class<? extends ObjectConverter<?>> value();
}
