package de.uni_augsburg.bazi.bmm;

import de.schummar.castable.Data;
import de.uni_augsburg.bazi.common.StringTable;
import de.uni_augsburg.bazi.common.algorithm.Algorithm;
import de.uni_augsburg.bazi.common.algorithm.Options;
import de.uni_augsburg.bazi.common.algorithm.VectorData;
import de.uni_augsburg.bazi.common.plain.PlainOptions;
import de.uni_augsburg.bazi.math.Int;

import java.util.List;
import java.util.function.BiFunction;

/** The base+min..max algorithm. */
public class BMMAlgorithm implements Algorithm<VectorData>
{
	/** Constraints for the seats of each party. */
	public final Int base, min, max;

	/** The algorithm to calculate the actual apportionment with. */
	public final Algorithm<? extends VectorData> method;

	/**
	 * Consreuctor with initielizers.
	 * @param base the base seats for each party.
	 * @param min the min seats for each party.
	 * @param max the max seats for each party.
	 * @param method the algorithm to calculate the actual apportionment with.
	 */
	public BMMAlgorithm(Int base, Int min, Int max, Algorithm<? extends VectorData> method)
	{
		this.base = base;
		this.min = min;
		this.max = max;
		this.method = method;
	}


	@Override public String name()	{		return method.name();	}
	@Override public Class<VectorData> dataType()	{		return VectorData.class;	}

	@Override public BiFunction<Data, PlainOptions, List<StringTable>> plainFormatter()
	{
		return (data, options) -> new BMMPlain(this, data.cast(VectorData.class), options).get();
	}

	@Override public void apply(Data data, Options options)
	{
		VectorData vecData = data.cast(VectorData.class);
		vecData.parties().forEach(
			p -> {
				p.min(min);
				p.max(max);
			}
		);
		vecData.seats(vecData.seats().sub(base.mul(vecData.parties().size())));

		method.apply(data, options);

		vecData.parties().forEach(
			p -> {
				p.seats(p.seats().add(base));
				p.min(null);
				p.max(null);
			}
		);
		vecData.seats(vecData.seats().add(base.mul(vecData.parties().size())));
	}
}
