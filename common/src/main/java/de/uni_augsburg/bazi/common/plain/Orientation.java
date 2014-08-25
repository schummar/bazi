package de.uni_augsburg.bazi.common.plain;

import de.schummar.castable.ConvertibleEnum;
import de.uni_augsburg.bazi.common.Resources;

/** The orientation in which the tables of the plain output should be printed. */
public enum Orientation implements ConvertibleEnum
{
	/** One row per party. */
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


	/** One row per pary. */
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


	/** Superapportionments vertical, matrix apportionments horizontal. */
	VERHOR
		{
			@Override public String key()
			{
				return "verhor";
			}
			@Override public boolean matches(String s)
			{
				return s.matches("ver(t(i[ck]al)?)?[/|\\\\-_]?hor(izontal)?");
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


	/** Superapportionments horizontal, matrix apportionments vertical. */
	HORVER
		{
			@Override public String key()
			{
				return "horver";
			}
			@Override public boolean matches(String s)
			{
				return s.matches("hor(izontal)?[/|\\\\-_]?ver(t(i[ck]al)?)?");
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


	/**
	 * Whether the vector apportionments should be printed vertically.
	 * @return true iff the vector apportionments should be printed vertically.
	 */
	public abstract boolean vectorVertical();

	/**
	 * Whether the matrix apportionments should be printed vertically.
	 * @return true iff the matrix apportionments should be printed vertically.
	 */
	public abstract boolean matrixVertical();
}
