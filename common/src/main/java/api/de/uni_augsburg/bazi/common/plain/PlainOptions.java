package de.uni_augsburg.bazi.common.plain;

import de.uni_augsburg.bazi.common.Plugin;
import de.uni_augsburg.bazi.common.data.Default;

/** Collection of Options for the plain output. */
public interface PlainOptions extends Plugin.Params
{
	/**
	 * The way divisors should be displayed in plain output.
	 * @return the way divisors should be displayed in plain output.
	 */
	@Default("quotients") DivisorFormat divisorFormat();

	/**
	 * The orientation in which the tables of the plain should be printed.
	 * @return the orientation in which the tables of the plain should be printed.
	 */
	@Default("verhor") Orientation orientation();

	/**
	 * The way ties should be represented in plain output.
	 * @return the way ties should be represented in plain output.
	 */
	@Default("coded") TieFormat tieFormat();

	/**
	 * The maximum number of decimal places to display in plain output.
	 * @return the maximum number of decimal places to display in plain output.
	 */
	@Default("10") Integer maxDigits();

	/**
	 * The label to top the party name column/row.
	 * @return the label to top the party name column/row.
	 */
	String nameLabel();

	/**
	 * The label to top the party vote column/row.
	 * @return the label to top the party vote column/row.
	 */
	String voteLabel();


	/**
	 * The way divisors should be displayed in plain output.
	 * @param divisorFormat the way divisors should be displayed in plain output.
	 */
	void divisorFormat(DivisorFormat divisorFormat);

	/**
	 * The orientation in which the tables of the plain should be printed.
	 * @param orientation the orientation in which the tables of the plain should be printed.
	 */
	void oritentation(Orientation orientation);

	/**
	 * The way ties should be represented in plain output.
	 * @param tieFormat the way ties should be represented in plain output.
	 */
	void tieFormat(TieFormat tieFormat);

	/**
	 * The maximum number of decimal places to display in plain output.
	 * @param maxDigits the maximum number of decimal places to display in plain output.
	 */
	void maxDigits(Integer maxDigits);

	/**
	 * The label to top the party name column/row.
	 * @param nameLabel the label to top the party name column/row.
	 */
	void nameLabel(String nameLabel);

	/**
	 * The label to top the party vote column/row.
	 * @param voteLabel the label to top the party vote column/row.
	 */
	void voteLabel(String voteLabel);
}
