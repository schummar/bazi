package de.uni_augsburg.bazi.common.algorithm;

import de.uni_augsburg.bazi.common.format.ConvertibleEnum;

public enum Uniqueness implements ConvertibleEnum
{
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
