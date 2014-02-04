package de.uni_augsburg.bazi.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Resources
{
	private static final Logger LOG = LoggerFactory.getLogger(Resources.class);
	private static final String RESOURCE_NAME = "de.uni_augsburg.bazi.common.strings";

	public static final ResourceBundle DEFAULT_RESOURCES = ResourceBundle.getBundle(RESOURCE_NAME, Locale.ENGLISH);
	private static ResourceBundle resources = DEFAULT_RESOURCES;


	public static void setLocale(Locale locale)
	{
		resources = ResourceBundle.getBundle(RESOURCE_NAME, locale);
	}


	public static String get(String key, Object... args)
	{
		try
		{
			return MessageFormat.format(resources.getString(key), args);
		}
		catch (MissingResourceException e) {}
		try
		{
			return MessageFormat.format(DEFAULT_RESOURCES.getString(key), args);
		}
		catch (MissingResourceException e) {}

		return MessageFormat.format("{} {}", key, Arrays.toString(args));
	}
}
