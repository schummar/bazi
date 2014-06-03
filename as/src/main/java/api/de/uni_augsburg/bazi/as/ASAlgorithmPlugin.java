package de.uni_augsburg.bazi.as;

import de.schummar.castable.Attribute;
import de.uni_augsburg.bazi.common.Plugin;
import de.uni_augsburg.bazi.divisor.DivisorAlgorithm;
import javafx.beans.property.Property;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/** This plugin produces instances of ASAlgorithm. */
public class ASAlgorithmPlugin implements Plugin<ASAlgorithm>
{
	@Override public Class<? extends ASAlgorithm> getInstanceType()
	{
		return ASAlgorithm.class;
	}
	@Override public List<Object> getParamAttributes()
	{
		return Collections.emptyList();
	}
	@Override public Optional<? extends ASAlgorithm> tryInstantiate(Plugin.Params params)
	{
		if (!params.name().equals("as")) return Optional.empty();

		Params asParams = params.cast(Params.class);
		DivisorAlgorithm Super = asParams.Super() != null ? asParams.Super() : asParams.method();
		DivisorAlgorithm sub = asParams.sub() != null ? asParams.sub() : asParams.method();
		return Optional.of(new ASAlgorithm(Super, sub, asParams.update()));
	}


	/** Parameters for an ASAlgorithm. */
	public interface Params extends Plugin.Params
	{
		/**
		 * The divisor algrithm to use for the super apportionment.
		 * @return the divisor algrithm to use for the super apportionment.
		 */
		@Attribute Property<DivisorAlgorithm> superProperty();
		default DivisorAlgorithm Super() { return superProperty().getValue(); }
		default void Super(DivisorAlgorithm v) { superProperty().setValue(v); }

		/**
		 * The divisor algrithm to use for the sub apportionment.
		 * @return the divisor algrithm to use for the sub apportionment.
		 */
		@Attribute Property<DivisorAlgorithm> subProperty();
		default DivisorAlgorithm sub() { return subProperty().getValue(); }
		default void sub(DivisorAlgorithm v) { subProperty().setValue(v); }

		/**
		 * The divisor algrithm to use for the super/sub if Super and/or sub are not specified apportionment.
		 * @return the divisor algrithm to use for the super/sub if Super and/or sub are not specified apportionment.
		 */
		@Attribute(def = "divstd") Property<DivisorAlgorithm> methodProperty();
		default DivisorAlgorithm method() { return methodProperty().getValue(); }
		default void method(DivisorAlgorithm v) { methodProperty().setValue(v); }

		/**
		 * The way the divisors are updates between iterations.
		 * @return the way the divisors are updates between iterations.
		 */
		@Attribute(def = "mid") Property<DivisorUpdateFunction> updateProperty();
		default DivisorUpdateFunction update() { return updateProperty().getValue(); }
		default void update(DivisorUpdateFunction v) { updateProperty().setValue(v); }
	}
}
