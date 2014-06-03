package de.uni_augsburg.bazi.dir;

import de.schummar.castable.Data;
import de.uni_augsburg.bazi.common.algorithm.Algorithm;
import de.uni_augsburg.bazi.common.algorithm.Filter;
import de.uni_augsburg.bazi.common.algorithm.VectorAlgorithm;

import java.util.Collections;
import java.util.List;

import static de.uni_augsburg.bazi.dir.DirData.Party;

/** The direct seats filter. */
public class DirFilter implements Filter
{
	@Override public boolean applicableGlobally()
	{
		return false;
	}
	@Override public boolean applicableTo(Algorithm algorithm)
	{
		return algorithm instanceof VectorAlgorithm;
	}
	@Override public List<Object> getInputAttributes()
	{
		return Collections.emptyList();
	}
	@Override public void preprocess(Data data)
	{ }
	@Override public void postprocess(Data data)
	{
		DirData dirData = data.cast(DirData.class);
		dirData.parties().forEach(DirFilter::sumDir);
		//dout.plain(new DirPlain(dout));
	}

	private static void sumDir(Party party)
	{
		party.parties().forEach(
			p -> {
				Party dp = p.cast(DirData.Party.class);
				sumDir(dp);
				party.dir(party.dir().add(dp.dir()));
			}
		);
	}
}
