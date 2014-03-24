package de.uni_augsburg.bazi.common.algorithm;

import de.uni_augsburg.bazi.common.data.Default;
import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Real;

import java.util.ArrayList;
import java.util.List;

/** A minimal vector output interface. */
public interface VectorOutput extends VectorInput
{
	@Override default List<? extends Party> parties() { return new ArrayList<>(); }

	/**
	 * The name of the apportionment.
	 * @param name the name of the apportionment.
	 */
	void name(String name);

	/**
	 * The number of seats to be apportioned.
	 * @param seats the number of seats to be distributed.
	 */
	void seats(Int seats);

	/**
	 * The list of parties the seats shall be apportioned among.
	 * @param parties the list of parties the seats shall be apportioned among.
	 */
	void parties(List<? extends Party> parties);


	/** An output party. */
	public interface Party extends VectorInput.Party
	{
		/**
		 * The number of seats the party received.
		 * @return the number of seats the party received.
		 */
		@Default("0") default Int seats() { return BMath.ZERO; }

		/**
		 * The uniqueness of the party's seats.
		 * @return the uniqueness of the party's seats.
		 */
		@Default("") default Uniqueness uniqueness() { return Uniqueness.UNIQUE; }

		/**
		 * Whether the party's {@link #min()} or {@link #max()} influenced the result.
		 * @return true iff the party's {@link #min()} or {@link #max()} influenced the result.
		 */
		@Default("false") default boolean conditionUsed() { return false; }

		/**
		 * The name of the party.
		 * @param name the name of the party.
		 */
		void name(String name);

		/**
		 * The votes of the party. Can be any non-negative real number.
		 * @param votes the votes of the party.
		 */
		void votes(Real votes);

		/**
		 * The minimum number of seats the party shall receive.
		 * @param min the minimum number of seats the party shall receive.
		 */
		void min(Int min);

		/**
		 * The maximum number of seats the party shall receive.
		 * @param max the maximum number of seats the party shall receive.
		 */
		void max(Int max);

		/**
		 * The number of seats the party received.
		 * @param seats the number of seats the party received.
		 */
		void seats(Int seats);

		/**
		 * The uniqueness of the party's seats.
		 * @param uniqueness the uniqueness of the party's seats.
		 */
		void uniqueness(Uniqueness uniqueness);

		/**
		 * Whether the party's {@link #min()} or {@link #max()} influenced the result.
		 * @param conditionUsed whether the party's {@link #min()} or {@link #max()} influenced the result.
		 */
		void conditionUsed(boolean conditionUsed);
	}
}
