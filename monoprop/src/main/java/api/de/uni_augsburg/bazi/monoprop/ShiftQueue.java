package de.uni_augsburg.bazi.monoprop;

import de.uni_augsburg.bazi.common.algorithm.Uniqueness;
import de.uni_augsburg.bazi.common.algorithm.VectorInput;
import de.uni_augsburg.bazi.common.algorithm.VectorOutput;
import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Real;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShiftQueue
{
	private final List<? extends VectorOutput.Party> parties;
	private final ShiftFunction shiftFunction;

	private final List<Integer> increase, decrease;

	public ShiftQueue(List<? extends VectorOutput.Party> parties, ShiftFunction shiftFunction)
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

	public void shift(Int n) throws NoShiftPossible
	{
		if (n.sgn() >= 0)
			n.timesDo(this::increase);
		else
			n.timesDo(this::decrease);
	}

	public void increase() throws NoShiftPossible
	{
		int i = nextIncrease();
		if (nextIncreaseValue().equals(BMath.ZERO))
			throw new NoShiftPossible();

		parties.get(i).seats(parties.get(i).seats().add(1));
		update();
	}

	public int nextIncrease()
	{
		return increase.get(0);
	}

	public Real nextIncreaseValue()
	{
		int i = nextIncrease();
		return shiftFunction.value(parties.get(i), parties.get(i).seats());
	}

	public void decrease() throws NoShiftPossible
	{
		int i = nextDecrease();
		if (nextDecreaseValue().equals(BMath.INF))
			throw new NoShiftPossible();

		parties.get(i).seats(parties.get(i).seats().sub(1));
		update();
	}

	public int nextDecrease()
	{
		return decrease.get(0);
	}

	public Real nextDecreaseValue()
	{
		int i = nextDecrease();
		return shiftFunction.value(parties.get(i), parties.get(i).seats().sub(1));
	}

	public void updateUniquenesses()
	{
		for (VectorOutput.Party party : parties)
			party.uniqueness(Uniqueness.UNIQUE);

		int lastIncrease = decrease.get(0);
		for (int i : increase)
			if (compare(parties.get(i), parties.get(i).seats(), parties.get(lastIncrease), parties.get(lastIncrease).seats().sub(1)) >= 0)
				parties.get(i).uniqueness(Uniqueness.CAN_BE_MORE);
			else
				break;

		int lastDecrease = increase.get(0);
		for (int i : decrease)
			if (compare(parties.get(i), parties.get(i).seats().sub(1), parties.get(lastDecrease), parties.get(lastDecrease).seats()) <= 0)
				parties.get(i).uniqueness(Uniqueness.CAN_BE_LESS);
			else
				break;
	}

	private void update()
	{
		Collections.sort(
			increase, (x, y) -> {
			int comp = -compare(parties.get(x), parties.get(x).seats(), parties.get(y), parties.get(y).seats());
			if (comp == 0)
				comp = x.compareTo(y);
			return comp;
		}
		);
		Collections.sort(
			decrease, (x, y) -> {
			int comp = compare(parties.get(x), parties.get(x).seats().sub(1), parties.get(y), parties.get(y).seats().sub(1));
			if (comp == 0)
				comp = -x.compareTo(y);
			return comp;
		}
		);
	}

	private int compare(VectorInput.Party p0, Int s0, VectorInput.Party p1, Int s1)
	{
		int compare = shiftFunction.value(p0, s0).compareTo(shiftFunction.value(p1, s1));
		if (compare != 0)
			return compare;
		return bias(p0, s0) - bias(p1, s1);
	}

	private static int bias(VectorInput.Party p, Int s)
	{
		if (s.sub(1).compareTo(p.min()) < 0)
			return 1;
		if (s.compareTo(p.max()) > 0)
			return -1;
		return 0;
	}

	public static interface ShiftFunction
	{
		public Real value(VectorInput.Party p, Int s);

		public default ShiftFunction mindConditions()
		{
			ShiftFunction that = this;
			return (party, seats) -> {
				if (seats.compareTo(party.min()) < 0)
					return BMath.INF;
				if (seats.compareTo(party.max()) > 0)
					return BMath.ZERO;
				return that.value(party, seats);
			};
		}
	}

	public static class NoShiftPossible extends RuntimeException
	{
		private static final long serialVersionUID = 1L;
	}
}
