package de.uni_augsburg.bazi.common.plain;

import de.schummar.castable.Attribute;
import de.uni_augsburg.bazi.common.Plugin;
import javafx.beans.property.Property;

/** Collection of Options for the plain output. */
public interface PlainOptions extends Plugin.Params
{
	/**
	 * The way divisors should be displayed in plain output.
	 * @return the way divisors should be displayed in plain output.
	 */
	@Attribute(def = "quotients") Property<DivisorFormat> divisorFormatProperty();
	default DivisorFormat divisorFormat() { return divisorFormatProperty().getValue(); }
	default void divisorFormat(DivisorFormat v) { divisorFormatProperty().setValue(v); }

	/**
	 * The orientation in which the tables of the plain should be printed.
	 * @return the orientation in which the tables of the plain should be printed.
	 */
	@Attribute(def = "verhor") Property<Orientation> orientationProperty();
	default Orientation orientation() { return orientationProperty().getValue(); }
	default void orientation(Orientation v) { orientationProperty().setValue(v); }

	/**
	 * The way ties should be represented in plain output.
	 * @return the way ties should be represented in plain output.
	 */
	@Attribute(def = "coded") Property<TieFormat> tieFormatProperty();
	default TieFormat tieFormat() { return tieFormatProperty().getValue(); }
	default void tieFormat(TieFormat v) { tieFormatProperty().setValue(v); }

	/**
	 * The maximum number of decimal places to display in plain output.
	 * @return the maximum number of decimal places to display in plain output.
	 */
	@Attribute(def = "10") Property<Integer> maxDigitsProperty();
	default Integer maxDigits() { return maxDigitsProperty().getValue(); }
	default void maxDigits(Integer v) { maxDigitsProperty().setValue(v); }

	/**
	 * The label to top the party name column/row.
	 * @return the label to top the party name column/row.
	 */
	@Attribute Property<String> nameLabelPropery();
	default String nameLabel() { return nameLabelPropery().getValue(); }
	default void nameLabel(String v) { nameLabelPropery().setValue(v); }

	/**
	 * The label to top the party vote column/row.
	 * @return the label to top the party vote column/row.
	 */
	@Attribute Property<String> voteLabelPropery();
	default String voteLabel() { return voteLabelPropery().getValue(); }
	default void voteLabel(String v) { voteLabelPropery().setValue(v); }
}
