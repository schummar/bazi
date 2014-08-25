package de.uni_augsburg.bazi.divisor;

import de.schummar.castable.Attribute;
import de.uni_augsburg.bazi.common.algorithm.VectorData;
import javafx.beans.property.Property;

/** Output of an DivisorAlgorithm. */
public interface DivisorData extends VectorData
{
	/**
	 * The divisor.
	 * @return the divisor.
	 */
	@Attribute Property<Divisor> divisorProperty();
	default Divisor divisor() { return divisorProperty().getValue(); }
	default void divisor(Divisor v) { divisorProperty().setValue(v); }
}
