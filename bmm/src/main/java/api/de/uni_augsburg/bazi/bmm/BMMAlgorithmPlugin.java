package de.uni_augsburg.bazi.bmm;

import de.schummar.castable.Attribute;
import de.uni_augsburg.bazi.common.Plugin;
import de.uni_augsburg.bazi.common.algorithm.Algorithm;
import de.uni_augsburg.bazi.math.Int;
import javafx.beans.property.Property;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/** This plugin produces instances of BMMAlgorithm. */
public class BMMAlgorithmPlugin implements Plugin<BMMAlgorithm>
{
	@Override public Class<? extends BMMAlgorithm> getInstanceType()
	{
		return BMMAlgorithm.class;
	}
	@Override public List<Object> getParamAttributes()
	{
		return Collections.emptyList();
	}
	@Override public Optional<? extends BMMAlgorithm> tryInstantiate(Plugin.Params params)
	{
		Params BMMParams = params.cast(Params.class);
		if (!params.name().matches("bmm|base\\+min\\.\\.max")) return Optional.empty();
		return Optional.of(new BMMAlgorithm(BMMParams.base(), BMMParams.min(), BMMParams.max(), BMMParams.method()));
	}


	/** Parameters for the BMMAlgorithm. */
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
		@Attribute(def = "divstd") Property<Algorithm> methodProperty();
		default Algorithm method() { return methodProperty().getValue(); }
		default void method(Algorithm v) { methodProperty().setValue(v); }
	}
}
