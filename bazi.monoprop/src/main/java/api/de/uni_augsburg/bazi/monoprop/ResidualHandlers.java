package de.uni_augsburg.bazi.monoprop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.common.collect.ImmutableList;

import de.uni_augsburg.bazi.common.Pair;
import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Rational;
import de.uni_augsburg.bazi.monoprop.QuotaMethod.ResidualHandler;

public enum ResidualHandlers implements ResidualHandler
{
	GREATEST_REMINDERS
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
			sort(indexAndFrac);


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
	},

	GREATEST_REMAINDERS_MIN
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
			sort(indexAndFrac);


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

	},

	WINNER_TAKES_ALL
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
			sort(indexAndWeight);


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

	};

	private static void sort(List<Pair<Integer, Rational>> indexAndWeight)
	{
		Collections.sort(indexAndWeight, new Comparator<Pair<Integer, Rational>>()
		{
			@Override public int compare(Pair<Integer, Rational> o1, Pair<Integer, Rational> o2)
			{
				return o2.getSecond().compareTo(o1.getSecond());
			}
		});
	}
}
