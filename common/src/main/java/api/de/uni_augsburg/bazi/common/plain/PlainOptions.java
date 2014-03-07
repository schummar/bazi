package de.uni_augsburg.bazi.common.plain;

import de.uni_augsburg.bazi.common.Plugin;

public interface PlainOptions extends Plugin.Params
{
	DivisorFormat divisorFormat();
	Orientation orientation();
	TieFormat tieFormat();
	Integer maxDigits();

	void divisorFormat(DivisorFormat divisorFormat);
	void oritentation(Orientation orientation);
	void tieFormat(TieFormat tieFormat);
	void maxDigits(Integer maxDigits);
}
