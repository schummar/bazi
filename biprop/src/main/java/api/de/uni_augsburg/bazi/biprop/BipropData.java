package de.uni_augsburg.bazi.biprop;

import de.schummar.castable.Attribute;
import de.uni_augsburg.bazi.common.algorithm.MatrixData;
import de.uni_augsburg.bazi.divisor.Divisor;
import de.uni_augsburg.bazi.divisor.DivisorData;
import javafx.beans.property.Property;

import java.util.List;
import java.util.Map;

/** Output of a biproportional method. */
public interface BipropData extends MatrixData
{
	/**
	 * The output of the super apportionment.
	 * @return the output of the super apportionment.
	 */
	@Attribute Property<DivisorData> superApportionmentProperty();
	default DivisorData superApportionment() { return superApportionmentProperty().getValue(); }
	default void superApportionment(DivisorData v) { superApportionmentProperty().setValue(v); }


	/**
	 * The party divisors.
	 * @return the party divisors.
	 */
	@Attribute Map<String, Divisor> partyDivisors();


	@Override @Attribute List<? extends DivisorData> districts();
}
