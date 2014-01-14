package de.uni_augsburg.bazi.monoprop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Real;
import de.uni_augsburg.bazi.monoprop.MonopropMethod.Input.Party;

public class ShiftQueue
{
	private final List<? extends Party> parties;
	private final List<Int> seats;
	private final ShiftFunction shiftFunction;

	private final List<Integer> increase, decrease;

	public ShiftQueue(List<? extends Party> parties, List<Int> seats, ShiftFunction shiftFunction)
	{
		this.parties = parties;
		this.seats = seats;
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

		seats.set(i, seats.get(i).add(1));
		update();
	}

	public int nextIncrease()
	{
		return increase.get(0);
	}

	public Real nextIncreaseValue()
	{
		int i = nextIncrease();
		return shiftFunction.value(parties.get(i), seats.get(i));
	}

	public void decrease() throws NoShiftPossible
	{
		int i = nextDecrease();
		if (nextDecreaseValue().equals(BMath.INF))
			throw new NoShiftPossible();

		seats.set(i, seats.get(i).sub(1));
		update();
	}

	public int nextDecrease()
	{
		return decrease.get(0);
	}

	public Real nextDecreaseValue()
	{
		int i = nextDecrease();
		return shiftFunction.value(parties.get(i), seats.get(i).sub(1));
	}

	public List<Uniqueness> getUniquenesses()
	{
		List<Uniqueness> uniquenesses = new ArrayList<>();
		for (int i = 0; i < parties.size(); i++)
			uniquenesses.add(Uniqueness.UNIQUE);

		int lastIncrease = decrease.get(0);
		for (int i : increase)
			if (compare(parties.get(i), seats.get(i), parties.get(lastIncrease), seats.get(lastIncrease).sub(1)) >= 0)
				uniquenesses.set(i, Uniqueness.CAN_BE_MORE);
			else
				break;

		int lastDecrease = increase.get(0);
		for (int i : decrease)
			if (compare(parties.get(i), seats.get(i).sub(1), parties.get(lastDecrease), seats.get(lastDecrease)) <= 0)
				uniquenesses.set(i, Uniqueness.CAN_BE_LESS);
			else
				break;

		return uniquenesses;
	}

	private void update()
	{
		Collections.sort(increase, (x, y) -> {
			int comp = -compare(parties.get(x), seats.get(x), parties.get(y), seats.get(y));
			if (comp == 0)
				comp = x.compareTo(y);
			return comp;
		});
		Collections.sort(decrease, (x, y) -> {
			int comp = compare(parties.get(x), seats.get(x).sub(1), parties.get(y), seats.get(y).sub(1));
			if (comp == 0)
				comp = -x.compareTo(y);
			return comp;
		});
	}

	private int compare(Party p0, Int s0, Party p1, Int s1)
	{
		int compare = shiftFunction.value(p0, s0).compareTo(shiftFunction.value(p1, s1));
		if (compare != 0)
			return compare;
		return bias(p0, s0) - bias(p1, s1);
	}

	private static int bias(MonopropMethod.Input.Party p, Int s)
	{
		if (s.sub(1).compareTo(p.getMin()) < 0)
			return 1;
		if (s.compareTo(p.getMax()) > 0)
			return -1;
		return 0;
	}

	public static interface ShiftFunction
	{
		public Real value(Party p, Int s);
	}

	public static class NoShiftPossible extends RuntimeException
	{
		private static final long serialVersionUID = 1L;
	}
}
