package de.uni_augsburg.bazi.common;

import java.util.Arrays;

public class Resources
{
	public static String get(String key, Object... args)
	{
		return String.format("%s %s", key, Arrays.toString(args));
	}
}
