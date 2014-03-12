package de.uni_augsburg.bazi.dir;

import de.uni_augsburg.bazi.common.data.Default;
import de.uni_augsburg.bazi.list.ListOutput;
import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;

import java.util.List;

/**
 * Created by Marco on 10.03.14.
 */
public interface DirOutput extends ListOutput
{
	@Override List<? extends Party> parties();


	public interface Party extends ListOutput.Party
	{
		@Default("0") default Int dir() { return BMath.ZERO; }
		void dir(Int dir);
	}
}
