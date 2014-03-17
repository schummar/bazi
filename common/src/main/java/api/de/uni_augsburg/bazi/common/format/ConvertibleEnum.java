package de.uni_augsburg.bazi.common.format;

public interface ConvertibleEnum
{
	String key();
	boolean matches(String s);
}
