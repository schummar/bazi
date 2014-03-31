package de.uni_augsburg.bazi.bmm_pow;

import de.uni_augsburg.bazi.common.Plugin;
import de.uni_augsburg.bazi.common.data.Default;
import de.uni_augsburg.bazi.divisor.DivisorAlgorithm;
import de.uni_augsburg.bazi.math.Int;

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
		@Default("divstd") DivisorAlgorithm method();
	}
}
