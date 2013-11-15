package de.uni_augsburg.bazi.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

class VersionImpl
{
	public static String getCurrentVersionName()
	{
		return getCurrentVersion().getName();
	}

	public static VersionImpl getCurrentVersion()
	{
		if (CURRENT != null)
			return CURRENT;

		synchronized (VersionImpl.class)
		{
			if (CURRENT != null)
				return CURRENT;

			VersionImpl v = null;
			Properties properties = new Properties();
			try (InputStream istream = VersionImpl.class.getResourceAsStream("version.properties"))
			{
				properties.load(istream);
				v = new VersionImpl(properties.getProperty("version"));
			}
			catch (IOException e)
			{
				e.printStackTrace();
				v = new VersionImpl("SNAPSHOT");
			}
			return CURRENT = v;
		}
	}


	private static VersionImpl CURRENT = null;
	private final String name;

	private VersionImpl(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}
}
