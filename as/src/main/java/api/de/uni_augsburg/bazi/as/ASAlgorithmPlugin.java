package de.uni_augsburg.bazi.as;

import de.uni_augsburg.bazi.common.Plugin;
import de.uni_augsburg.bazi.common.data.Default;
import de.uni_augsburg.bazi.divisor.DivisorAlgorithm;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/** This plugin produces instances of ASAlgorithm. */
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


	/** Parameters for an ASAlgorithm. */
	public interface Params extends Plugin.Params
	{
		/**
		 * The divisor algrithm to use for the super apportionment.
		 * @return the divisor algrithm to use for the super apportionment.
		 */
		DivisorAlgorithm Super();

		/**
		 * The divisor algrithm to use for the sub apportionment.
		 * @return the divisor algrithm to use for the sub apportionment.
		 */
		DivisorAlgorithm sub();

		/**
		 * The divisor algrithm to use for the super/sub if Super and/or sub are not specified apportionment.
		 * @return the divisor algrithm to use for the super/sub if Super and/or sub are not specified apportionment.
		 */
		@Default("divstd") DivisorAlgorithm method();

		/**
		 * The way the divisors are updates between iterations.
		 * @return the way the divisors are updates between iterations.
		 */
		@Default("mid") DivisorUpdateFunction update();
	}
}
