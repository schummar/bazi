package de.uni_augsburg.bazi.common.plain;

import de.uni_augsburg.bazi.common.Resources;
import de.uni_augsburg.bazi.common.format.ConvertibleEnum;

/**
 * Created by Marco on 07.03.14.
 */
public enum Orientation implements ConvertibleEnum
{
	VERTICAL
		{
			@Override public String key()
			{
				return "ver";
			}
			@Override public boolean matches(String s)
			{
				return s.matches("ver(t(i[ck]al)?)?");
			}
			@Override public String toString()
			{
				return Resources.get("orientation.ver");
			}
		},


	HORIZONTAL
		{
			@Override public String key()
			{
				return "hor";
			}
			@Override public boolean matches(String s)
			{
				return s.matches("hor(izontal)?");
			}
			@Override public String toString()
			{
				return Resources.get("orientation.hor");
			}
		},


	VERHOR
		{
			@Override public String key()
			{
				return "verhor";
			}
			@Override public boolean matches(String s)
			{
				return s.matches("ver(t(i[ck]al)?)?[/|\\-_]?hor(izontal)?");
			}
			@Override public String toString()
			{
				return Resources.get("orientation.verhor");
			}
		},


	HORVER
		{
			@Override public String key()
			{
				return "horver";
			}
			@Override public boolean matches(String s)
			{
				return s.matches("hor(izontal)?[/|\\-_]?ver(t(i[ck]al)?)?");
			}
			@Override public String toString()
			{
				return Resources.get("orientation.horver");
			}
		}
}
