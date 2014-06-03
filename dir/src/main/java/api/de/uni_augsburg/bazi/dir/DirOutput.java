package de.uni_augsburg.bazi.dir;

import de.uni_augsburg.bazi.common.data.Default;
import de.uni_augsburg.bazi.list.ListData;
import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;

import java.util.List;

/** Output of the DirFilter */
public interface DirOutput extends ListData
{
	@Override List<? extends Party> parties();


	public interface Party extends ListData.Party
	{
		/**
		 * The number of direct seats the party gets.
		 * @return the number of direct seats the party gets.
		 */
		@Default("0") default Int dir() { return BMath.ZERO; }

		/**
		 * The number of direct seats the party gets.
		 * @param dir the number of direct seats the party gets.
		 */
		void dir(Int dir);
	}
}
