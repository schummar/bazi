package de.uni_augsburg.bazi.bmm_pow;

import de.schummar.castable.Attribute;
import de.schummar.castable.CProperty;
import de.schummar.castable.Data;
import de.uni_augsburg.bazi.common.StringTable;
import de.uni_augsburg.bazi.common.algorithm.Algorithm;
import de.uni_augsburg.bazi.common.algorithm.Options;
import de.uni_augsburg.bazi.common.plain.PlainOptions;
import de.uni_augsburg.bazi.divisor.DivisorAlgorithm;
import de.uni_augsburg.bazi.math.Int;

import java.util.List;
import java.util.function.BiFunction;

/** The base+min..max(pow) algorithm. */
public interface BMMPowAlgorithm extends Algorithm<BMMPowData>, Data
{
	/**
	 * The base seats for each party.
	 * @return the base seats for each party.
	 */
	@Attribute(def = "0") CProperty<Int> baseProperty();
	default Int base() { return baseProperty().getValue(); }
	default void base(Int v) { baseProperty().setValue(v); }

	/**
	 * The min seats for each party.
	 * @return the min seats for each party.
	 */
	@Attribute(def = "0") CProperty<Int> minProperty();
	default Int min() { return minProperty().getValue(); }
	default void min(Int v) { minProperty().setValue(v); }

	/**
	 * The max seats for each party.
	 * @return the max seats for each party.
	 */
	@Attribute(def = "oo") CProperty<Int> maxProperty();
	default Int max() { return maxProperty().getValue(); }
	default void max(Int v) { maxProperty().setValue(v); }

	/**
	 * The algorithm to calculate the actual apportionment with.
	 * @return the algorithm to calculate the actual apportionment with.
	 */
	@Attribute(def = "divstd") CProperty<DivisorAlgorithm> methodProperty();
	default DivisorAlgorithm method() { return methodProperty().getValue(); }
	default void method(DivisorAlgorithm v) { methodProperty().setValue(v); }

	@Override default String name() { return String.format("BMM(POW) - %s+%s..%s", base(), min(), max()); }
	@Override default Class<BMMPowData> dataType() { return BMMPowData.class; }

	@Override default BiFunction<Data, PlainOptions, List<StringTable>> plainFormatter()
	{
		return (data, options) -> new BMMPowPlain(method(), data.cast(BMMPowData.class), options).get();
	}

	@Override default void apply(Data in, Options options)
	{
		BMMPowAlgorithmImpl.calculate(in.cast(BMMPowData.class), method(), base(), min(), max(), options);
	}
}
