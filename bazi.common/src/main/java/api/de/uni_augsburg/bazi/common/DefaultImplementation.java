package de.uni_augsburg.bazi.common;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/** Defines a default implementation class for deserialization */
@Retention(RetentionPolicy.RUNTIME) public @interface DefaultImplementation
{
	Class<?> value();
}
