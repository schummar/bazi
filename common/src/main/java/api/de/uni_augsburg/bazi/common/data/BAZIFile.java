package de.uni_augsburg.bazi.common.data;

import de.schummar.castable.Attribute;
import de.schummar.castable.Data;
import de.uni_augsburg.bazi.common.algorithm.Algorithm;
import javafx.beans.property.Property;

public interface BAZIFile extends Data
{
	@Attribute Data output();

	@Attribute Property<Algorithm<?>> algorithmProperty();
	default Algorithm<?> algorithm() { return algorithmProperty().getValue(); }
	default void algorithm(Algorithm<?> v) { algorithmProperty().setValue(v); }

	@Attribute Property<String> infoProperty();
	default String info() { return infoProperty().getValue(); }
	default void info(String v) { infoProperty().setValue(v); }
}
