package de.uni_augsburg.bazi.common.algorithm;

import de.uni_augsburg.bazi.common.data.Data;
import de.uni_augsburg.bazi.common.data.Default;
import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Real;

import java.util.Collections;
import java.util.List;

/** A minimal vector input interface. */
public interface VectorInput extends Data
{
	/**
	 * The name of the apportionment.
	 * @return the name of the apportionment.
	 */
	@Default("") String name();

	/**
	 * The number of seats to be apportioned.
	 * @return the number of seats to be distributed.
	 */
	@Default("0") default Int seats() { return BMath.ZERO; }

	/**
	 * The list of parties the seats shall be apportioned among.
	 * @return the list of parties the seats shall be apportioned among.
	 */
	default List<? extends Party> parties()
	{
		return Collections.emptyList();
	}


	/** An input party. */
	public interface Party extends Data
	{
		/**
		 * The name of the party.
		 * @return the name of the party.
		 */
		@Default("") String name();

		/**
		 * The votes of the party. Can be any non-negative real number.
		 * @return the votes of the party.
		 */
		@Default("0") default Real votes() { return BMath.ZERO; }

		/**
		 * The minimum number of seats the party shall receive.
		 * @return the minimum number of seats the party shall receive.
		 */
		@Default("0") default Int min() { return BMath.ZERO; }

		/**
		 * The maximum number of seats the party shall receive.
		 * @return the maximum number of seats the party shall receive.
		 */
		@Default("oo") default Int max() { return BMath.INF; }
	}
}
