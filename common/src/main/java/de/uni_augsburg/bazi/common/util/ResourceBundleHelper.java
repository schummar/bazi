package de.uni_augsburg.bazi.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.function.Function;


public class ResourceBundleHelper
{
	public static ResourceBundle getUTF8Bundle(String basename, Locale locale, ClassLoader loader)
	{
		return ResourceBundle.getBundle(basename, locale, loader, LANG_CONTROL);
	}


	private static final ResourceBundle.Control LANG_CONTROL = new ResourceBundle.Control()
	{
		@Override
		public ResourceBundle newBundle(
			String baseName,
			Locale locale,
			String format,
			ClassLoader loader,
			boolean reload
		) throws IllegalAccessException, InstantiationException, IOException
		{
			String bundleName = toBundleName(baseName, locale);
			String resourceName = toResourceName(bundleName, "lang");
			ResourceBundle bundle = null;
			InputStream stream = null;
			if (reload)
			{
				URL url = loader.getResource(resourceName);
				if (url != null)
				{
					URLConnection connection = url.openConnection();
					if (connection != null)
					{
						connection.setUseCaches(false);
						stream = connection.getInputStream();
					}
				}
			}
			else
			{
				stream = loader.getResourceAsStream(resourceName);
			}
			if (stream != null)
			{
				try
				{
					bundle = new PropertyResourceBundle(new InputStreamReader(stream, "UTF-8"));
				}
				finally
				{
					stream.close();
				}
			}
			return bundle;
		}
	};
}
