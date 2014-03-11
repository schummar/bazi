package de.uni_augsburg.bazi.dir;

import de.uni_augsburg.bazi.common.algorithm.Algorithm;
import de.uni_augsburg.bazi.common.algorithm.Filter;
import de.uni_augsburg.bazi.common.algorithm.VectorAlgorithm;
import de.uni_augsburg.bazi.common.data.Data;
import de.uni_augsburg.bazi.common.util.CollectionHelper;

import java.util.Collections;
import java.util.List;

/**
 * Created by Marco on 10.03.14.
 */
public class DirFilter implements Filter
{
	@Override public boolean applicableGlobally()
	{
		return false;
	}
	@Override public boolean applicableTo(Algorithm algorithm)
	{
		return algorithm instanceof VectorAlgorithm<?>;
	}
	@Override public List<Object> getInputAttributes()
	{
		return Collections.emptyList();
	}
	@Override public void preprocess(Data in)
	{ }
	@Override public void postprocess(Data in, Data out)
	{
		DirOutput din = in.cast(DirOutput.class);
		DirOutput dout = out.cast(DirOutput.class);

		CollectionHelper.forEachPair(din.parties(), dout.parties(), (ip, op) -> op.dir(ip.dir()));
		dout.plain(new DirPlain(dout));
	}
}
