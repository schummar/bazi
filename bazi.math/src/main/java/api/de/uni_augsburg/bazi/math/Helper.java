package de.uni_augsburg.bazi.math;

import org.apfloat.Apint;
import org.apfloat.Aprational;
import org.apfloat.AprationalMath;

public class Helper
{
	public static ARational parseString(String s)
	{
		try
		{
			return new AInt(s);
		}
		catch (NumberFormatException e)
		{}
		return new ARational(s);
	}

	static Aprational aprational(String s)
	{
		try
		{
			return new Aprational(s);
		}
		catch (NumberFormatException e)
		{}

		try
		{
			int dot = s.indexOf(".");
			int scale = -(s.length() - dot - 1);
			String num = s.replaceAll("\\.", "");
			return AprationalMath.scale(new Apint(num), scale);
		}
		catch (NumberFormatException e)
		{}

		throw new NumberFormatException();
	}
}
