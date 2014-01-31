package de.uni_augsburg.bazi.common;

import java.util.Arrays;

public class Resources
{
	public static String get(String key, Object... args)
	{
		if (args.length > 0)
			return String.format("%s %s", key, Arrays.toString(args));
		return key;
	}
}
