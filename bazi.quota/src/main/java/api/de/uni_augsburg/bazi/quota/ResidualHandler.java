package de.uni_augsburg.bazi.quota;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.common.collect.ImmutableList;

import de.uni_augsburg.bazi.common.Pair;
import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Rational;
import de.uni_augsburg.bazi.monoprop.Uniqueness;

public interface ResidualHandler
{
	public ImmutableList<Pair<Integer, Uniqueness>> getIncrementsAndUniqueness(ImmutableList<Pair<Int, Rational>> seatsAndRests, int residual);

	public static final ResidualHandler
			GREATEST_REMINDERS = new GreatestReminders(),
			GREATEST_REMAINDERS_MIN = new GreatestRemindersMin(),
			WINNER_TAKES_ALL = new WinnerTakesAll();


	// //////////////////////////////////////////////////////////////////////////


	public static class GreatestReminders implements ResidualHandler
	{
		@Override public ImmutableList<Pair<Integer, Uniqueness>> getIncrementsAndUniqueness(ImmutableList<Pair<Int, Rational>> seatsAndRests, int residual)
		{
			List<Pair<Integer, Rational>> indexAndFrac = new ArrayList<>();
			List<Pair<Integer, Uniqueness>> result = new ArrayList<>();
			for (int i = 0; i < seatsAndRests.size(); i++)
			{
				indexAndFrac.add(Pair.of(i, seatsAndRests.get(i).getSecond()));
				result.add(Pair.of(0, Uniqueness.UNIQUE));
			}
			Collections.sort(indexAndFrac, RESIDUAL_COMPARATOR);


			Rational tieLeft = indexAndFrac.get(residual - 1).getSecond();
			Rational tieRight = residual < indexAndFrac.size() ? indexAndFrac.get(residual).getSecond() : BMath.MINUS_ONE;

			for (int i = 0; i < residual; i++)
			{
				int index = indexAndFrac.get(i).getFirst();
				Rational frac = indexAndFrac.get(i).getSecond();
				result.set(index, Pair.of(1, frac.equals(tieRight) ? Uniqueness.CAN_BE_LESS : Uniqueness.UNIQUE));
			}

			for (int i = residual; i < indexAndFrac.size(); i++)
			{
				int index = indexAndFrac.get(i).getFirst();
				Rational frac = indexAndFrac.get(i).getSecond();
				result.set(index, Pair.of(0, frac.equals(tieLeft) ? Uniqueness.CAN_BE_MORE : Uniqueness.UNIQUE));
			}

			return ImmutableList.copyOf(result);
		}
	}


	// //////////////////////////////////////////////////////////////////////////


	public static class GreatestRemindersMin implements ResidualHandler
	{
		@Override public ImmutableList<Pair<Integer, Uniqueness>> getIncrementsAndUniqueness(ImmutableList<Pair<Int, Rational>> seatsAndRests, int residual)
		{
			List<Pair<Integer, Rational>> indexAndFrac = new ArrayList<>();
			List<Pair<Integer, Uniqueness>> result = new ArrayList<>();
			for (int i = 0; i < seatsAndRests.size(); i++)
			{
				Rational frac = seatsAndRests.get(i).getFirst().compareTo(0) > 0 ? seatsAndRests.get(i).getSecond() : BMath.MINUS_ONE;
				indexAndFrac.add(Pair.of(i, frac));
				result.add(Pair.of(0, Uniqueness.UNIQUE));
			}
			Collections.sort(indexAndFrac, RESIDUAL_COMPARATOR);


			Rational tieLeft = indexAndFrac.get(residual - 1).getSecond();
			Rational tieRight = residual < indexAndFrac.size() ? indexAndFrac.get(residual).getSecond() : BMath.MINUS_ONE;

			for (int i = 0; i < residual; i++)
			{
				int index = indexAndFrac.get(i).getFirst();
				Rational frac = indexAndFrac.get(i).getSecond();
				if (frac.equals(-1))
					throw new RuntimeException("No solution because no more party is eligable to receive a residual seat."); // TODO res
				result.set(index, Pair.of(1, frac.equals(tieRight) ? Uniqueness.CAN_BE_LESS : Uniqueness.UNIQUE));
			}

			for (int i = residual; i < indexAndFrac.size(); i++)
			{
				int index = indexAndFrac.get(i).getFirst();
				Rational frac = indexAndFrac.get(i).getSecond();
				result.set(index, Pair.of(0, frac.equals(tieLeft) ? Uniqueness.CAN_BE_MORE : Uniqueness.UNIQUE));
			}

			return ImmutableList.copyOf(result);
		}
	}


	// //////////////////////////////////////////////////////////////////////////


	public static class WinnerTakesAll implements ResidualHandler
	{
		@Override public ImmutableList<Pair<Integer, Uniqueness>> getIncrementsAndUniqueness(ImmutableList<Pair<Int, Rational>> seatsAndRests, int residual)
		{
			List<Pair<Integer, Rational>> indexAndWeight = new ArrayList<>();
			List<Pair<Integer, Uniqueness>> result = new ArrayList<>();
			for (int i = 0; i < seatsAndRests.size(); i++)
			{
				indexAndWeight.add(Pair.of(i, seatsAndRests.get(i).getFirst().add(seatsAndRests.get(i).getSecond())));
				result.add(Pair.of(0, Uniqueness.UNIQUE));
			}
			Collections.sort(indexAndWeight, RESIDUAL_COMPARATOR);


			Rational tieLeft = indexAndWeight.get(0).getSecond();
			Rational tieRight = indexAndWeight.size() > 1 ? indexAndWeight.get(1).getSecond() : BMath.MINUS_ONE;

			for (int i = 0; i < 1; i++)
			{
				int index = indexAndWeight.get(i).getFirst();
				Rational frac = indexAndWeight.get(i).getSecond();
				result.set(index, Pair.of(residual, frac.equals(tieRight) ? Uniqueness.CAN_BE_LESS : Uniqueness.UNIQUE));
			}

			for (int i = 1; i < indexAndWeight.size(); i++)
			{
				int index = indexAndWeight.get(i).getFirst();
				Rational frac = indexAndWeight.get(i).getSecond();
				result.set(index, Pair.of(0, frac.equals(tieLeft) ? Uniqueness.CAN_BE_MORE : Uniqueness.UNIQUE));
			}

			return ImmutableList.copyOf(result);
		}
	}


	// //////////////////////////////////////////////////////////////////////////


	static Comparator<Pair<Integer, Rational>> RESIDUAL_COMPARATOR = new Comparator<Pair<Integer, Rational>>()
	{
		@Override public int compare(Pair<Integer, Rational> o1, Pair<Integer, Rational> o2)
		{
			return o2.getSecond().compareTo(o1.getSecond());
		}
	};
}
