package de.uni_augsburg.bazi.biprop;

import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Real;
import de.uni_augsburg.bazi.monoprop.Divisor;

public interface DivisorUpdateFunction
{
	public Real update(Real divisor, Divisor newDivisor, Int fault);

	public static DivisorUpdateFunction
		MIDPOINT = (d, nd, f) -> d.mul(nd.nice()),

	RANDOM = (d, nd, f) -> d.mul(
		nd.max().equals(BMath.INF)
			? nd.min().add(nd.nice().sub(nd.min()).mul(BMath.random()))
			: nd.min().add(nd.max().sub(nd.min()).mul(BMath.random()))
	),

	EXTREME = (d, nd, f) -> {
		if (f.compareTo(0) > 0 && !nd.max().equals(BMath.INF))
			return d.mul(nd.max());
		if (f.compareTo(0) < 0)
			return d.mul(nd.min());
		return d.mul(nd.nice());
	};
}
