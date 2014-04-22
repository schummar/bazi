package de.uni_augsburg.bazi.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/** Utility class for localized String resources. */
public class Resources
{
	private Resources() {}


	private static final Logger LOG = LoggerFactory.getLogger(Resources.class);
	private static final String RESOURCE_NAME = "de.uni_augsburg.bazi.common.bazi";
	private static ResourceBundle resources = ResourceBundle.getBundle(RESOURCE_NAME);


	/**
	 * Set the locale to the fiven one.
	 * @param locale the new locale.
	 */
	public static void setLocale(Locale locale)
	{
		resources = ResourceBundle.getBundle(RESOURCE_NAME, locale);
		LOG.info("Set locale: {}", locale);
	}


	/**
	 * Get a localized String for the given key.
	 * @param key the key of the localized String.
	 * @param args these arguments' string representations fill in placeholders in the localized String.
	 * (E.g. 'text {0}, more text {1}')
	 * @return the localized String.
	 */
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
