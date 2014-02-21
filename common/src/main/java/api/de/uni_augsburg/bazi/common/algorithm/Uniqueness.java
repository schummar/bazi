package de.uni_augsburg.bazi.common.algorithm;

public enum Uniqueness
{
	CAN_BE_MORE
		{
			@Override
			public String toString()
			{
				return "+";
			}
		},

	UNIQUE
		{
			@Override
			public String toString()
			{
				return "";
			}
		},

	CAN_BE_LESS
		{
			@Override
			public String toString()
			{
				return "-";
			}
		}
}
