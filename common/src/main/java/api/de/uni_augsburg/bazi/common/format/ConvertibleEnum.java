package de.uni_augsburg.bazi.common.format;

/**
 * Created by Marco on 07.03.14.
 */
public interface ConvertibleEnum
{
	String key();
	boolean matches(String s);
}
