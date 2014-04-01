package de.uni_augsburg.bazi.separate;

import de.uni_augsburg.bazi.common.Plugin;
import de.uni_augsburg.bazi.common.algorithm.VectorAlgorithm;
import de.uni_augsburg.bazi.common.data.Default;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/** This plugin produces instances of SeparateAlgorithm. */
public class SeparateAlgorithmPlugin implements Plugin<SeparateAlgorithm>
{
	@Override public Class<? extends SeparateAlgorithm> getInstanceType()
	{
		return SeparateAlgorithm.class;
	}
	@Override public List<Object> getParamAttributes()
	{
		return Collections.emptyList();
	}
	@Override public Optional<? extends SeparateAlgorithm> tryInstantiate(Plugin.Params params)
	{
		if (!params.name().equals("separate")) return Optional.empty();
		return Optional.of(new SeparateAlgorithm(params.cast(Params.class).method()));
	}


	/** Parameters for SeparateAlgorithm. */
	public interface Params extends Plugin.Params
	{
		/**
		 * The method used for the district apportionments.
		 * @return the method used for the district apportionments.
		 */
		@Default("divstd") VectorAlgorithm<?> method();
	}
}
