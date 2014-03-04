package de.uni_augsburg.bazi.biprop;

import de.uni_augsburg.bazi.common.Plugin;
import de.uni_augsburg.bazi.monoprop.DivisorAlgorithm;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by Marco on 04.03.14.
 */
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
		Params cast = params.cast(Params.class);
		if (params.name().equals("as"))
			return Optional.of(new ASAlgorithm(cast.div(), cast.update()));
		return Optional.empty();
	}


	public interface Params extends Plugin.Params
	{
		DivisorAlgorithm div();
		DivisorUpdateFunction update();
	}
}
