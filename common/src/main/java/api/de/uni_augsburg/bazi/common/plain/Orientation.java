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
			@Override public boolean vectorVertical()
			{
				return true;
			}
			@Override public boolean matrixVertical()
			{
				return true;
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
			@Override public boolean vectorVertical()
			{
				return false;
			}
			@Override public boolean matrixVertical()
			{
				return false;
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
			@Override public boolean vectorVertical()
			{
				return true;
			}
			@Override public boolean matrixVertical()
			{
				return false;
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
			@Override public boolean vectorVertical()
			{
				return false;
			}
			@Override public boolean matrixVertical()
			{
				return true;
			}
		};

	public abstract boolean vectorVertical();
	public abstract boolean matrixVertical();
}
