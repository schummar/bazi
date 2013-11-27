package de.uni_augsburg.bazi.monoprop;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Rational;

public class QuotaMethodTest
{
	public static void main(String[] args)
	{
		final int seats = 1;
		final int[] votes = { 1, 1 };
		final String[] names = { "A", "B" };

		MonopropMethod.Input input = new MonopropMethod.Input()
		{
			@Override public Int getSeats()
			{
				return BMath.value(seats);
			}

			@Override public ImmutableList<? extends Party> getParties()
			{
				List<Party> parties = new ArrayList<>();
				for (int i = 0; i < votes.length; i++)
				{
					final int j = i;
					parties.add(new Party()
					{
						@Override public Rational getVotes()
						{
							return BMath.value(votes[j]);
						}

						@Override public String getName()
						{
							return names[j];
						}

						@Override public Int getMin()
						{
							return BMath.ZERO;
						}

						@Override public Int getMax()
						{
							return BMath.ZERO;
						}

						@Override public Int getDir()
						{
							return BMath.ZERO;
						}
					});
				}
				return ImmutableList.copyOf(parties);
			}
		};

		QuotaMethod qm = new QuotaMethod(QuotaFunctions.HARE, ResidualHandlers.GREATEST_REMINDERS);
		MonopropMethod.Output output = qm.calculate(input);


		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		System.out.println(gson.toJson(output));
	}
}
