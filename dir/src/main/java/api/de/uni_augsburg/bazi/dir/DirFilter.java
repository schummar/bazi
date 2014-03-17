package de.uni_augsburg.bazi.dir;

import de.uni_augsburg.bazi.common.algorithm.Algorithm;
import de.uni_augsburg.bazi.common.algorithm.Filter;
import de.uni_augsburg.bazi.common.algorithm.VectorAlgorithm;
import de.uni_augsburg.bazi.common.data.Data;
import de.uni_augsburg.bazi.common.util.CollectionHelper;

import java.util.Collections;
import java.util.List;

import static de.uni_augsburg.bazi.dir.DirOutput.Party;

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
		DirOutput din = in.copy().cast(DirOutput.class);
		DirOutput dout = out.cast(DirOutput.class);

		din.parties().forEach(DirFilter::sumDir);
		CollectionHelper.forEachPair(din.parties(), dout.parties(), (ip, op) -> op.dir(ip.dir()));
		dout.plain(new DirPlain(dout));
	}

	public static void sumDir(Party party)
	{
		party.parties().forEach(
			p -> {
				Party dp = p.cast(DirOutput.Party.class);
				sumDir(dp);
				party.dir(party.dir().add(dp.dir()));
			}
		);
	}
}
