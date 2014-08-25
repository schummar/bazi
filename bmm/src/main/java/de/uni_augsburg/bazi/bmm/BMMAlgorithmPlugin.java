package de.uni_augsburg.bazi.bmm;

import de.uni_augsburg.bazi.common.Plugin;

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
		return Optional.of(params.cast(BMMAlgorithm.class));
	}
}
