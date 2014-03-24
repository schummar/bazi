package de.uni_augsburg.bazi.common.plain;

import de.uni_augsburg.bazi.common.Resources;
import de.uni_augsburg.bazi.common.format.ConvertibleEnum;

/** The way ties should be represented in plain output. */
public enum TieFormat implements ConvertibleEnum
{
	CODED
		{
			@Override public String key()
			{
				return "coded";
			}
			@Override public boolean matches(String s)
			{
				return s.matches("code(d)?");
			}
			@Override public String toString()
			{
				return Resources.get("tieformat.coded");
			}
		},


	CODED_LIST
		{
			@Override public String key()
			{
				return "list";
			}
			@Override public boolean matches(String s)
			{
				return s.matches("(coded[/|\\\\-_]?)?list");
			}
			@Override public String toString()
			{
				return Resources.get("tieformat.coded_list");
			}
		},


	LIST
		{
			@Override public String key()
			{
				return "uncoded_list";
			}
			@Override public boolean matches(String s)
			{
				return s.matches("uncoded[/|\\\\-_]?list");
			}
			@Override public String toString()
			{
				return Resources.get("tieformat.list");
			}
		}
}
