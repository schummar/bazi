package de.uni_augsburg.bazi.dir;

import de.uni_augsburg.bazi.common.algorithm.VectorInput;
import de.uni_augsburg.bazi.common.algorithm.VectorOutput;
import de.uni_augsburg.bazi.math.Int;

import java.util.List;

/**
 * Created by Marco on 10.03.14.
 */
public interface DirOutput extends VectorInput
{
	@Override List<? extends Party> parties();


	public interface Party extends VectorOutput.Party
	{
		Int dir();
		void dir(Int dir);
	}
}
