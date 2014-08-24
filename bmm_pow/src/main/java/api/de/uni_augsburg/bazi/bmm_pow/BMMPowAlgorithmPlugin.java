package de.uni_augsburg.bazi.bmm_pow;

import de.uni_augsburg.bazi.common.Plugin;

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
		return Optional.of(params.cast(BMMPowAlgorithm.class));
	}
}
