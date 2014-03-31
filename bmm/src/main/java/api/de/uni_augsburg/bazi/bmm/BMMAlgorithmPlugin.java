package de.uni_augsburg.bazi.bmm;

import de.uni_augsburg.bazi.common.Plugin;
import de.uni_augsburg.bazi.common.algorithm.VectorAlgorithm;
import de.uni_augsburg.bazi.common.data.Default;
import de.uni_augsburg.bazi.math.Int;

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
		if (!params.name().matches("bmm|base\\+min\\.\\.max")) return Optional.empty();

		Params bmmParams = params.cast(Params.class);
		return Optional.of(new BMMAlgorithm(bmmParams.base(), bmmParams.min(), bmmParams.max(), bmmParams.method()));
	}


	/** Parameters for the BMMAlgorithm. */
	public interface Params extends Plugin.Params
	{
		/**
		 * The base seats for each party.
		 * @return the base seats for each party.
		 */
		@Default("0") Int base();

		/**
		 * The min seats for each party.
		 * @return the min seats for each party.
		 */
		@Default("0") Int min();

		/**
		 * The max seats for each party.
		 * @return the max seats for each party.
		 */
		@Default("oo") Int max();

		/**
		 * The algorithm to calculate the actual apportionment with.
		 * @return the algorithm to calculate the actual apportionment with.
		 */
		@Default("divstd") VectorAlgorithm<?> method();
	}
}
