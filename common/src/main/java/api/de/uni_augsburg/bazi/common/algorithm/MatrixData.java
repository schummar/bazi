package de.uni_augsburg.bazi.common.algorithm;

import de.schummar.castable.Attribute;
import de.schummar.castable.Data;
import javafx.beans.property.Property;

import java.util.List;

/** A minimal maxtrix input interface. */
public interface MatrixData extends Data
{
	/**
	 * The name of the apportionment.
	 * @return the name of the apportionment.
	 */
	@Attribute Property<String> nameProperty();
	default String name() { return nameProperty().getValue(); }
	default void name(String v) { nameProperty().setValue(v); }

	/**
	 * The list of one dimensional inputs which represent one district each.
	 * @return the list of one dimensional inputs which represent one district each.
	 */
	List<? extends VectorData> districts();
}
