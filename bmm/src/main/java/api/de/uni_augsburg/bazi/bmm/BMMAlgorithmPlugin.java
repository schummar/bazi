package de.uni_augsburg.bazi.bmm;

import de.uni_augsburg.bazi.common.Plugin;
import de.uni_augsburg.bazi.common.algorithm.VectorAlgorithm;
import de.uni_augsburg.bazi.common.data.Default;
import de.uni_augsburg.bazi.math.Int;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by Marco on 12.03.14.
 */
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


	public interface Params extends Plugin.Params
	{
		@Default("0") Int base();
		@Default("0") Int min();
		@Default("oo") Int max();
		@Default("divstd") VectorAlgorithm<?> method();
	}
}
