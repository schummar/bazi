package de.uni_augsburg.bazi.list;

import de.uni_augsburg.bazi.common.Plugin;
import de.uni_augsburg.bazi.common.algorithm.VectorAlgorithm;
import de.uni_augsburg.bazi.common.data.Default;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ListAlgorithmPlugin implements Plugin<ListAlgorithm>
{
	@Override public Class<? extends ListAlgorithm> getInstanceType() { return ListAlgorithm.class; }
	@Override public List<Object> getParamAttributes() { return Collections.emptyList(); }

	@Override public Optional<ListAlgorithm> tryInstantiate(Plugin.Params params)
	{
		if (!params.name().equals("list")) return Optional.empty();

		Params listParams = params.cast(Params.class);
		VectorAlgorithm<?> Super = listParams.Super() != null ? listParams.Super() : listParams.method();
		VectorAlgorithm<?> sub = listParams.sub() != null ? listParams.sub() : listParams.method();
		return Optional.of(new ListAlgorithm(Super, sub));
	}


	public interface Params extends Plugin.Params
	{
		VectorAlgorithm<?> Super();
		VectorAlgorithm<?> sub();
		@Default("divstd") VectorAlgorithm<?> method();
	}
}
