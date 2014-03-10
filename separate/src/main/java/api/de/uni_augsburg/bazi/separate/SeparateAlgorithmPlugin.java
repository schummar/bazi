package de.uni_augsburg.bazi.separate;

import de.uni_augsburg.bazi.common.Plugin;
import de.uni_augsburg.bazi.common.algorithm.VectorAlgorithm;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by Marco on 10.03.14.
 */
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
		return Optional.of(new SeparateAlgorithm(params.cast(Params.class).vector()));
	}


	public interface Params extends Plugin.Params
	{
		VectorAlgorithm<?> vector();
	}
}
