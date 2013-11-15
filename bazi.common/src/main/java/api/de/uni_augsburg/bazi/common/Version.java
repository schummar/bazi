package de.uni_augsburg.bazi.common;


public abstract class Version
{
	public static String getCurrentVersionName()
	{
		return VersionImpl.getCurrentVersionName();
	}
}
