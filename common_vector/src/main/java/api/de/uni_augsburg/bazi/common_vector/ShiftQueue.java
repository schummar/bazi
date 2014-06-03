package de.uni_augsburg.bazi.common_vector;

import de.uni_augsburg.bazi.common.algorithm.Uniqueness;
import de.uni_augsburg.bazi.common.algorithm.VectorData;
import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Real;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Manages increases or decreases of seats. The order is determined by a given ShiftFunction. */
public class ShiftQueue
{
	private final List<? extends VectorData.Party> parties;
	private final ShiftFunction shiftFunction;

	private final List<Integer> increase, decrease;

	/**
	 * Constructor with initializers.
	 * @param parties the list of parties.
	 * @param shiftFunction the shift function that determindes the order in which to increase/decrease.
	 */
	public ShiftQueue(List<? extends VectorData.Party> parties, ShiftFunction shiftFunction)
	{
		this.parties = parties;
		this.shiftFunction = shiftFunction;
		this.increase = new ArrayList<>();
		this.decrease = new ArrayList<>();
		for (int i = 0; i < parties.size(); i++)
		{
			increase.add(i);
			decrease.add(i);
		}
		update();
	}

	/**
	 * If n is positive performs n increases. If n is negative performs -n decreases.
	 * @param n the number of shifts.
	 * @throws NoShiftPossible if no more increases/decreases are possible.
	 * (Because conditions would be violated etc.)
	 */
	public void shift(Int n) throws NoShiftPossible
	{
		if (n.sgn() >= 0)
			n.timesDo(this::increase);
		else
			n.timesDo(this::decrease);
	}


	/**
	 * Performes an increase.
	 * @throws NoShiftPossible if no more increases are possible.
	 * (Because conditions would be violated etc.)
	 */
	public void increase() throws NoShiftPossible
	{
		int i = nextIncrease();
		if (nextIncreaseValue().equals(BMath.ZERO))
			throw new NoShiftPossible();

		parties.get(i).seats(parties.get(i).seats().add(1));
		update();
	}

	/**
	 * Returns the index of the next increase.
	 * @return the index of the next increase.
	 */
	public int nextIncrease()
	{
		return increase.get(0);
	}

	/**
	 * Returns the value the shift function produces for the next increase.
	 * @return the value the shift function produces for the next increase.
	 */
	public Real nextIncreaseValue()
	{
		int i = nextIncrease();
		return shiftFunction.value(parties.get(i), parties.get(i).seats(), true);
	}


	/**
	 * Performes an decrease.
	 * @throws NoShiftPossible if no more decreases are possible.
	 * (Because conditions would be violated etc.)
	 */
	public void decrease() throws NoShiftPossible
	{
		int i = nextDecrease();
		if (nextDecreaseValue().equals(BMath.INF))
			throw new NoShiftPossible();

		parties.get(i).seats(parties.get(i).seats().sub(1));
		update();
	}

	/**
	 * Returns the index of the next decrease.
	 * @return the index of the next decrease.
	 */
	public int nextDecrease()
	{
		return decrease.get(0);
	}

	/**
	 * Returns the value the shift function produces for the next decrease.
	 * @return the value the shift function produces for the next decrease.
	 */
	public Real nextDecreaseValue()
	{
		int i = nextDecrease();
		return shiftFunction.value(parties.get(i), parties.get(i).seats().sub(1), false);
	}


	/** Sets the uniquenesses for all parties. */
	public void updateUniquenesses()
	{
		for (VectorData.Party party : parties)
			party.uniqueness(Uniqueness.UNIQUE);

		int lastIncrease = decrease.get(0);
		for (int i : increase)
			if (compare(parties.get(i), parties.get(i).seats(), parties.get(lastIncrease), parties.get(lastIncrease).seats().sub(1), true) >= 0)
				parties.get(i).uniqueness(Uniqueness.CAN_BE_MORE);
			else
				break;

		int lastDecrease = increase.get(0);
		for (int i : decrease)
			if (compare(parties.get(i), parties.get(i).seats().sub(1), parties.get(lastDecrease), parties.get(lastDecrease).seats(), false) <= 0)
				parties.get(i).uniqueness(Uniqueness.CAN_BE_LESS);
			else
				break;
	}


	private void update()
	{
		Collections.sort(
			increase, (x, y) -> {
				int comp = -compare(parties.get(x), parties.get(x).seats(), parties.get(y), parties.get(y).seats(), true);
				if (comp == 0)
					comp = x.compareTo(y);
				return comp;
			}
		);
		Collections.sort(
			decrease, (x, y) -> {
				int comp = compare(parties.get(x), parties.get(x).seats().sub(1), parties.get(y), parties.get(y).seats().sub(1), false);
				if (comp == 0)
					comp = -x.compareTo(y);
				return comp;
			}
		);
	}


	private int compare(VectorData.Party p0, Int s0, VectorData.Party p1, Int s1, boolean increase)
	{
		int compare = shiftFunction.value(p0, s0, increase).compareTo(shiftFunction.value(p1, s1, increase));
		if (compare != 0)
			return compare;
		return bias(p0, s0) - bias(p1, s1);
	}


	private static int bias(VectorData.Party p, Int s)
	{
		if (s.sub(1).compareTo(p.min()) < 0)
			return 1;
		if (s.compareTo(p.max()) > 0)
			return -1;
		return 0;
	}


	/**
	 * A ShiftFunction returns a Real value that represents the priority of a party
	 * with a certain number of seats getting an increase.
	 * The party with the highest value for its current number of seats is increased first.
	 * The party with the lowest value for its current number of seats minus one is decreased first.
	 */
	public static interface ShiftFunction
	{
		/**
		 * Returns the priority of a given party with a given number of seats.
		 * @param p the party.
		 * @param s the number of seats.
		 * @param increase whether the party competes for an increase (if false: a decrease).
		 * @return the priority of a given party with a given number of seats.
		 */
		public Real value(VectorData.Party p, Int s, boolean increase);

		/**
		 * Returns this shift function with the addition that 0/oo values are given if
		 * an increase/decrease would mean violating a condition.
		 * @return this shift function with the addition that 0/oo values are given if
		 * an increase/decrease would mean violating a condition.
		 */
		public default ShiftFunction mindConditions()
		{
			ShiftFunction that = this;
			return (party, seats, increase) -> {
				if (seats.compareTo(party.min()) < 0
					|| (seats.equals(party.min()) && !increase))
					return BMath.INF;
				if (seats.compareTo(party.max()) > 0
					|| (seats.equals(party.max()) && increase))
					return BMath.ZERO;
				return that.value(party, seats, increase);
			};
		}
	}

	/**
	 * If no more increases/decreases are possible. (Because conditions would be violated etc.)
	 */
	public static class NoShiftPossible extends RuntimeException
	{
		private static final long serialVersionUID = 1L;
	}
}
