package de.uni_augsburg.bazi.bmm_pow;

import de.schummar.castable.Attribute;
import de.uni_augsburg.bazi.common.Plugin;
import de.uni_augsburg.bazi.divisor.DivisorAlgorithm;
import de.uni_augsburg.bazi.math.Int;
import javafx.beans.property.Property;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/** This plugin produces instances of BMMPowAlgorithm. */
public class BMMPowAlgorithmPlugin implements Plugin<BMMPowAlgorithm>
{
	@Override public Class<? extends BMMPowAlgorithm> getInstanceType()
	{
		return BMMPowAlgorithm.class;
	}
	@Override public List<Object> getParamAttributes()
	{
		return Collections.emptyList();
	}
	@Override public Optional<? extends BMMPowAlgorithm> tryInstantiate(Plugin.Params params)
	{
		if (!params.name().matches("(bmm|base\\+min\\.\\.max)[/|\\\\-_]?p(ow)?")) return Optional.empty();

		Params bmmParams = params.cast(Params.class);
		return Optional.of(new BMMPowAlgorithm(bmmParams.base(), bmmParams.min(), bmmParams.max(), bmmParams.method()));
	}

	/** Parameters for the BMMPowAlgorithm. */
	public interface Params extends Plugin.Params
	{
		/**
		 * The base seats for each party.
		 * @return the base seats for each party.
		 */
		@Attribute Property<Int> baseProperty();
		default Int base() { return baseProperty().getValue(); }
		default void base(Int v) { baseProperty().setValue(v); }

		/**
		 * The min seats for each party.
		 * @return the min seats for each party.
		 */
		@Attribute Property<Int> minProperty();
		default Int min() { return minProperty().getValue(); }
		default void min(Int v) { minProperty().setValue(v); }

		/**
		 * The max seats for each party.
		 * @return the max seats for each party.
		 */
		@Attribute(def = "oo") Property<Int> maxProperty();
		default Int max() { return maxProperty().getValue(); }
		default void max(Int v) { maxProperty().setValue(v); }

		/**
		 * The algorithm to calculate the actual apportionment with.
		 * @return the algorithm to calculate the actual apportionment with.
		 */
		@Attribute(def = "divstd") Property<DivisorAlgorithm> methodProperty();
		default DivisorAlgorithm method() { return methodProperty().getValue(); }
		default void method(DivisorAlgorithm v) { methodProperty().setValue(v); }
	}
}
