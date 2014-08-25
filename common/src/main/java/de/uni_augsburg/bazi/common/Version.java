package de.uni_augsburg.bazi.common;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/** A BAZI version number. */
public class Version
{
	/**
	 * The name of the current (now running) version.
	 * @return the name of the current (now running) version.
	 */
	public static String getCurrentVersionName()
	{
		return getCurrentVersion().getName();
	}

	/**
	 * The current (now running) version.
	 * @return the current (now running) version.
	 */
	public static Version getCurrentVersion()
	{
		if (CURRENT != null)
			return CURRENT;

		synchronized (Version.class)
		{
			if (CURRENT != null)
				return CURRENT;

			Version v = null;
			Properties properties = new Properties();
			try (InputStream istream = Version.class.getResourceAsStream("version.properties"))
			{
				properties.load(istream);
				v = new Version(properties.getProperty("version"));
			}
			catch (IOException e)
			{
				e.printStackTrace();
				v = new Version("SNAPSHOT");
			}
			return CURRENT = v;
		}
	}


	private static Version CURRENT = null;
	private final String name;

	private Version(String name)
	{
		this.name = name;
	}

	/**
	 * The version's name.
	 * @return the version's name.
	 */
	public String getName()
	{
		return name;
	}
}
