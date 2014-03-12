package de.uni_augsburg.bazi.common.plain;

import de.uni_augsburg.bazi.common.Plugin;
import de.uni_augsburg.bazi.common.data.Default;

public interface PlainOptions extends Plugin.Params
{
	@Default("quotients") DivisorFormat divisorFormat();
	@Default("verhor") Orientation orientation();
	@Default("coded") TieFormat tieFormat();
	@Default("10") Integer maxDigits();
	String nameLabel();
	String voteLabel();

	void divisorFormat(DivisorFormat divisorFormat);
	void oritentation(Orientation orientation);
	void tieFormat(TieFormat tieFormat);
	void maxDigits(Integer maxDigits);
	void nameLabel(String nameLabel);
	void voteLabel(String voteLabel);
}
