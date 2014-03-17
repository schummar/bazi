package de.uni_augsburg.bazi.common.plain;

import de.uni_augsburg.bazi.common.Resources;
import de.uni_augsburg.bazi.common.format.ConvertibleEnum;

public enum DivisorFormat implements ConvertibleEnum
{
	DIV_SPLIT
		{
			@Override public String key()
			{
				return "div_split";
			}
			@Override public boolean matches(String s)
			{
				return s.matches("div|split|quo|div[/|\\\\-_]?(split|quo)");
			}
			@Override public String toString()
			{
				return Resources.get("divisorformat.div_split");
			}
		},


	INTERVAL
		{
			@Override public String key()
			{
				return "interval";
			}
			@Override public boolean matches(String s)
			{
				return s.matches("i(nter)?val");
			}
			@Override public String toString()
			{
				return Resources.get("divisorformat.interval");
			}
		},


	MULT
		{
			@Override public String key()
			{
				return "mult";
			}
			@Override public boolean matches(String s)
			{
				return s.matches("mul(t(iplier)?)?");
			}
			@Override public String toString()
			{
				return Resources.get("divisorformat.mult");
			}
		},


	MULT_INTERVAL
		{
			@Override public String key()
			{
				return "mult_interval";
			}
			@Override public boolean matches(String s)
			{
				return s.matches("m(ul(t)?)?[/|\\\\-_]?i(nter)?val");
			}
			@Override public String toString()
			{
				return Resources.get("divisorformat.mult_interval");
			}
		},


	QUOTIENTS
		{
			@Override public String key()
			{
				return "quotients";
			}
			@Override public boolean matches(String s)
			{
				return s.matches("quot(ient(s)?)?");
			}
			@Override public String toString()
			{
				return Resources.get("divisorformat.quotients");
			}
		}
}
