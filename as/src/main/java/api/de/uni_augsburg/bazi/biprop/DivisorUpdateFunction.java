package de.uni_augsburg.bazi.biprop;

import de.uni_augsburg.bazi.common.format.Converter;
import de.uni_augsburg.bazi.common.format.ObjectConverter;
import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Real;
import de.uni_augsburg.bazi.monoprop.Divisor;

@Converter(DivisorUpdateFunction.Converter.class)
public interface DivisorUpdateFunction
{
	public Real apply(Divisor divisor, Int fault);

	public static DivisorUpdateFunction
		MIDPOINT = (d, f) -> d.nice(),

	RANDOM = (d, f) -> d.max().equals(BMath.INF)
		? d.min().add(d.nice().sub(d.min()).mul(BMath.random()))
		: d.min().add(d.max().sub(d.min()).mul(BMath.random())),

	EXTREME = (d, f) -> {
		if (f.compareTo(0) > 0 && !d.max().equals(BMath.INF))
			return d.max();
		if (f.compareTo(0) < 0)
			return d.min();
		return d.nice();
	};


	public static class Converter implements ObjectConverter<DivisorUpdateFunction>
	{
		@Override public Object serialize(DivisorUpdateFunction value)
		{
			return "mid";
		}
		@Override public DivisorUpdateFunction deserialize(Object value)
		{
			return MIDPOINT;
		}
	}
}
