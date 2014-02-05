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
	private static final String RESOURCE_NAME = "de.uni_augsburg.bazi.common.messages";
	private static ResourceBundle resources = ResourceBundle.getBundle(RESOURCE_NAME);

	public static void setLocale(Locale locale)
	{
		resources = ResourceBundle.getBundle(RESOURCE_NAME, locale);
		LOG.info("Set locale: {}", locale);
	}


	public static String get(String key, Object... args)
	{
		try
		{
			return MessageFormat.format(resources.getString(key), args);
		}
		catch (MissingResourceException e)
		{
			LOG.warn("Missing Resource Key: {}", key);
			return MessageFormat.format("{0} {1}", key, Arrays.toString(args));
		}
	}
}
