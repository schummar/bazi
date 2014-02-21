package de.uni_augsburg.bazi.common.format;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
* Created by Marco on 21.02.14.
*/
@Retention(RetentionPolicy.RUNTIME)
public @interface Converter
{
	Class<? extends ObjectConverter<?>> value();
}
