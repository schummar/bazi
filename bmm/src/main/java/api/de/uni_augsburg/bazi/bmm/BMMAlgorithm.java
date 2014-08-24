package de.uni_augsburg.bazi.bmm;

import de.schummar.castable.Attribute;
import de.schummar.castable.CProperty;
import de.schummar.castable.Data;
import de.uni_augsburg.bazi.common.StringTable;
import de.uni_augsburg.bazi.common.algorithm.Algorithm;
import de.uni_augsburg.bazi.common.algorithm.Options;
import de.uni_augsburg.bazi.common.algorithm.VectorData;
import de.uni_augsburg.bazi.common.plain.PlainOptions;
import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;
import javafx.beans.property.Property;

import java.util.List;
import java.util.function.BiFunction;

/** The base+min..max algorithm. */
public interface BMMAlgorithm extends Algorithm<VectorData>, Data
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
	@Attribute(def = "divstd") CProperty<Algorithm<? extends VectorData>> methodProperty();
	default Algorithm<? extends VectorData> method() { return methodProperty().getValue(); }
	default void method(Algorithm<? extends VectorData> v) { methodProperty().setValue(v); }


	@Override default String name() { return method().name(); }
	@Override default Class<VectorData> dataType() { return VectorData.class; }

	@Override default BiFunction<Data, PlainOptions, List<StringTable>> plainFormatter()
	{
		return (data, options) -> new BMMPlain(this, data.cast(VectorData.class), options).get();
	}

	@Override default void apply(Data data, Options options)
	{
		VectorData vecData = data.cast(VectorData.class);
		vecData.parties().forEach(
			p -> {
				p.min(min());
				p.max(max());
			}
		);
		vecData.seats(vecData.seats().sub(base().mul(vecData.parties().size())));

		method().apply(data, options);

		vecData.parties().forEach(
			p -> {
				p.seats(p.seats().add(base()));
				p.min(BMath.ZERO);
				p.max(BMath.INF);
			}
		);
		vecData.seats(vecData.seats().add(base().mul(vecData.parties().size())));
	}
}
