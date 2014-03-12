package de.uni_augsburg.bazi.bmm;

import de.uni_augsburg.bazi.common.algorithm.Options;
import de.uni_augsburg.bazi.common.algorithm.VectorAlgorithm;
import de.uni_augsburg.bazi.common.algorithm.VectorOutput;
import de.uni_augsburg.bazi.common.data.Data;
import de.uni_augsburg.bazi.math.Int;

import java.util.Collections;
import java.util.List;

/**
 * Created by Marco on 12.03.14.
 */
public class BMMAlgorithm extends VectorAlgorithm<VectorOutput>
{
	public final Int base, min, max;
	public final VectorAlgorithm<?> method;
	public BMMAlgorithm(Int base, Int min, Int max, VectorAlgorithm<?> method)
	{
		this.base = base;
		this.min = min;
		this.max = max;
		this.method = method;
	}


	@Override public String name()
	{
		return method.name();
	}
	@Override public List<Object> getInputAttributes()
	{
		return Collections.emptyList();
	}


	@Override public VectorOutput applyUnfiltered(Data in, Options options)
	{
		VectorOutput data = in.cast(VectorOutput.class);
		data.parties().forEach(
			p -> {
				p.min(min);
				p.max(max);
			}
		);
		data.seats(data.seats().sub(base.mul(data.parties().size())));

		data = method.applyUnfiltered(data, options);

		data.parties().forEach(
			p -> {
				p.seats(p.seats().add(base));
			}
		);
		data.seats(data.seats().add(base.mul(data.parties().size())));

		data.plain(new BMMPlain(data, this));
		return data;
	}
}
