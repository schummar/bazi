package de.uni_augsburg.bazi.common.plain;

import de.schummar.castable.ConvertibleEnum;
import de.uni_augsburg.bazi.common.Resources;

/** The way divisors should be displayed in plain output. */
public enum DivisorFormat implements ConvertibleEnum
{
	/** Display the divisor (nice) or split under the seat column. */
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


	/** Display the divisor or split interval under the seat column. */
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


	/** Display the multiplier under the seat column. */
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


	/** Display the multiplier interval under the seat column. */
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


	/** Display the divisor (nice) or split under an extra qutient column. */
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
