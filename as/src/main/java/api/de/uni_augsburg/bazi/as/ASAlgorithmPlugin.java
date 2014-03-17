package de.uni_augsburg.bazi.as;

import de.uni_augsburg.bazi.common.Plugin;
import de.uni_augsburg.bazi.common.data.Default;
import de.uni_augsburg.bazi.divisor.DivisorAlgorithm;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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


	public interface Params extends Plugin.Params
	{
		DivisorAlgorithm Super();
		DivisorAlgorithm sub();
		@Default("divstd") DivisorAlgorithm method();
		@Default("mid") DivisorUpdateFunction update();
	}
}
