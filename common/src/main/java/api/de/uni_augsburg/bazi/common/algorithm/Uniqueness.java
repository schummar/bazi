package de.uni_augsburg.bazi.common.algorithm;

import de.uni_augsburg.bazi.common.format.ConvertibleEnum;

/** Is the number of seats in an apportionment unique or could it be more/less? */
public enum Uniqueness implements ConvertibleEnum
{
	/** The number of seats could be increased just as well. */
	CAN_BE_MORE
		{
			@Override public String key()
			{
				return "+";
			}
			@Override public boolean matches(String s)
			{
				return s.equals("+");
			}
			@Override public String toString()
			{
				return "+";
			}
		},

	/** The number of seats is unique. */
	UNIQUE
		{
			@Override public String key()
			{
				return "";
			}
			@Override public boolean matches(String s)
			{
				return s.equals("");
			}
			@Override
			public String toString()
			{
				return "";
			}
		},

	/** The number of seats could be decreased just as well. */
	CAN_BE_LESS
		{
			@Override public String key()
			{
				return "-";
			}
			@Override public boolean matches(String s)
			{
				return s.matches("-");
			}
			@Override
			public String toString()
			{
				return "-";
			}
		}
}
