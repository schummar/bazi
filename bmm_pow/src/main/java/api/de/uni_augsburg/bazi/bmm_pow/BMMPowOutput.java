package de.uni_augsburg.bazi.bmm_pow;

import de.schummar.castable.Attribute;
import de.uni_augsburg.bazi.common.algorithm.OutputList;
import de.uni_augsburg.bazi.common.algorithm.VectorData;
import de.uni_augsburg.bazi.divisor.DivisorData;
import de.uni_augsburg.bazi.math.Real;
import javafx.beans.property.Property;

import java.util.List;

/** Output iof the BMMPow Algorithm. */
public interface BMMPowOutput extends OutputList, VectorData
{
	@Override @Attribute List<? extends BMMPowResult> results();

	/** One possible result (for one possible power). */
	public interface BMMPowResult extends DivisorData
	{
		/**
		 * The power the votes were exponentiated with.
		 * @return the power the votes were exponentiated with.
		 */
		@Attribute Property<Real> powerProperty();
		default Real power() { return powerProperty().getValue(); }
		default void power(Real v) { powerProperty().setValue(v); }
	}
}
