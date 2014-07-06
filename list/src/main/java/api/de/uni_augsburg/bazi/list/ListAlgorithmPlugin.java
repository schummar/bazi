package de.uni_augsburg.bazi.list;

import de.schummar.castable.Attribute;
import de.uni_augsburg.bazi.common.Plugin;
import de.uni_augsburg.bazi.common.algorithm.Algorithm;
import de.uni_augsburg.bazi.common.algorithm.VectorData;
import javafx.beans.property.Property;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/** This plugin produces instances of ListAlgorithm. */
public class ListAlgorithmPlugin implements Plugin<ListAlgorithm>
{
	@Override public Class<? extends ListAlgorithm> getInstanceType() { return ListAlgorithm.class; }
	@Override public List<Object> getParamAttributes() { return Collections.emptyList(); }

	@Override public Optional<ListAlgorithm> tryInstantiate(Plugin.Params params)
	{
		if (!params.name().equals("list")) return Optional.empty();

		Params listParams = params.cast(Params.class);
		Algorithm<? extends VectorData> Super = listParams.Super() != null ? listParams.Super() : listParams.method();
		Algorithm<? extends VectorData> sub = listParams.sub() != null ? listParams.sub() : listParams.method();
		return Optional.of(new ListAlgorithm(Super, sub));
	}


	/** Parameters for a ListAlgorithm. */
	public interface Params extends Plugin.Params
	{
		/**
		 * The algorithm used for the main apportionment.
		 * @return the algorithm used for the main apportionment.
		 */
		@Attribute Property<Algorithm<? extends VectorData>> superProperty();
		default Algorithm<? extends VectorData> Super() { return superProperty().getValue(); }
		default void Super(Algorithm<? extends VectorData> v) { superProperty().setValue(v); }

		/**
		 * The algorithm used for the sub apportionment.
		 * @return the algorithm used for the sub apportionment.
		 */
		@Attribute Property<Algorithm<? extends VectorData>> subProperty();
		default Algorithm<? extends VectorData> sub() { return subProperty().getValue(); }
		default void sub(Algorithm<? extends VectorData> v) { subProperty().setValue(v); }

		/**
		 * The algorithm used for the all apportionments without own algorithms.
		 * @return the algorithm used for the all apportionments without own algorithms.
		 */
		@Attribute(def = "divstd") Property<Algorithm<? extends VectorData>> methodProperty();
		default Algorithm<? extends VectorData> method() { return methodProperty().getValue(); }
		default void method(Algorithm<? extends VectorData> v) { methodProperty().setValue(v); }
	}
}
