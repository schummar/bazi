package de.uni_augsburg.bazi.list;

import de.uni_augsburg.bazi.common.Plugin;
import de.uni_augsburg.bazi.common.algorithm.VectorAlgorithm;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ListAlgorithmPlugin implements Plugin<ListAlgorithm>
{
	@Override public Class<? extends ListAlgorithm> getInstanceType() { return ListAlgorithm.class; }
	@Override public List<Object> getParamAttributes() { return Collections.emptyList(); }

	@Override public Optional<ListAlgorithm> tryInstantiate(Plugin.Params params)
	{
		Params cast = params.cast(Params.class);
		return params.name().toLowerCase().equals("list")
			? Optional.of(new ListAlgorithm(cast.main(), cast.sub()))
			: Optional.empty();
	}


	public interface Params extends Plugin.Params
	{
		VectorAlgorithm main();
		VectorAlgorithm sub();
	}
}
