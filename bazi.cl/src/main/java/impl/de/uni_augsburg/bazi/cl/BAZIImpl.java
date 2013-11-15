package de.uni_augsburg.bazi.cl;

import de.uni_augsburg.bazi.common.Version;


public class BAZIImpl
{
	public static void main(String[] args)
	{
		System.out.println("BAZI under development...");
		System.out.println(String.format("The current Version is: %s", Version.getCurrentVersionName()));
	}
}
