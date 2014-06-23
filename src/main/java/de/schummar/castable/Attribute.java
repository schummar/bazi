package de.schummar.castable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Attribute
{
	public static final String NULL = "dkldjakjdchxövaksölsdglkh.andk,sdhgkshck.sajdlsdigsdkljalcjailsdzukg";
	String def() default NULL;
	Class<? extends Converter> converter() default Converter.class;
	Class<? extends Converter> contentConverter() default Converter.class;
}
