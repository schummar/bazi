package de.uni_augsburg.bazi.common.format;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Converter
{
	Class<? extends ObjectConverter<?>> value();
}
