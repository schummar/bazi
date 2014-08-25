package de.uni_augsburg.bazi.quota;

import de.schummar.castable.Attribute;
import de.uni_augsburg.bazi.common.algorithm.VectorData;
import de.uni_augsburg.bazi.math.Real;
import javafx.beans.property.Property;

/** Output of the QuotaAlgorithm. */
public interface QuotaData extends VectorData
{
	/**
	 * The quota.
	 * @return the quota.
	 */
	@Attribute Property<Real> quotaProperty();
	default Real quota() { return quotaProperty().getValue(); }
	default void quota(Real v) { quotaProperty().setValue(v); }
}
