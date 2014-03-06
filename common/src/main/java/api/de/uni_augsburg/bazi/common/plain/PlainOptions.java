package de.uni_augsburg.bazi.common.plain;

public class PlainOptions
{
	public final Orientation orientation;
	public final DivisorFormat divisorFormat;
	public final TieFormat tieFormat;

	public PlainOptions(Orientation orientation, DivisorFormat divisorFormat, TieFormat tieFormat)
	{
		this.orientation = orientation;
		this.divisorFormat = divisorFormat;
		this.tieFormat = tieFormat;
	}


	public enum Orientation
	{
		HORIZONTAL, VERTICAL, HORVERT, VERTHOR
	}

	public enum DivisorFormat
	{
		DIV_QUO, DIV_INTERVAL, MULT, MULT_INTERVAL, QUOTIENT
	}

	public enum TieFormat
	{
		CODED, CODED_LIST, LIST
	}
}
