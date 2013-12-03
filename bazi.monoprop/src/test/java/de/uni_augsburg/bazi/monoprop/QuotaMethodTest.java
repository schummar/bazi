package de.uni_augsburg.bazi.monoprop;

import java.io.IOException;
import java.util.Arrays;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Rational;
import de.uni_augsburg.bazi.math.Real;

public class QuotaMethodTest
{
	private static final Logger log = Logger.getLogger(QuotaMethodTest.class);

	public static void main(String[] args) throws IOException, NoSuchMethodException, SecurityException
	{
		BasicConfigurator.configure();
		log.debug("begin");
		//
		// String json = Resources.toString(Resources.getResource(QuotaMethodTest.class, "test_input.bazi"), Charsets.UTF_8);
		// log.debug("Warnings: " + Json.checkJson(json, PlainInput.class));
		//
		// MonopropMethod.Input input = Json.fromJson(json, PlainInput.class);
		// MonopropMethod qm = new QuotaMethod(QuotaFunction.HARE, ResidualHandler.GREATEST_REMINDERS);
		//
		// log.debug("start calculation");
		// MonopropMethod.Output output = qm.calculate(input);
		// log.debug("end calculation");
		//
		// log.debug(Json.toJson(output));
		// log.debug("end");

		Int allSeats = BMath.intOf(4);
		final Int[] seats = { BMath.ZERO, BMath.ZERO, BMath.ZERO };
		final Int[] min = { BMath.intOf(2), BMath.intOf(0), BMath.intOf(0) };
		final Rational[] weights = { BMath.rationalOf("1"), BMath.rationalOf("1"), BMath.rationalOf("1") };
		final RoundingFunction.Stationary rf = new RoundingFunction.Stationary(BMath.HALF, null);


		PriorityQueue pq = new PriorityQueue(seats.length)
		{
			@Override public Real increaseValue(int i)
			{
				return weights[i].div(rf.getBorder(seats[i]));
			}
			@Override public Real decreaseValue(int i)
			{
				return weights[i].div(rf.getBorder(seats[i].sub(1)));
			}
			@Override public int increasePreference(int i)
			{
				if (seats[i].compareTo(min[i]) < 0)
					return 1;
				return 0;
			}
			@Override public int decreasePreference(int i)
			{
				if (seats[i].compareTo(min[i]) <= 0)
					return -1;
				return 0;
			}
		};


		for (Int sum = BMath.ZERO; sum.compareTo(allSeats) < 0; sum = sum.add(1))
		{
			int i = pq.nextIncrease();
			seats[i] = seats[i].add(1);
		}
		Uniqueness[] un = new Uniqueness[seats.length];
		for (int i = 0; i < un.length; i++)
			un[i] = pq.getUniqueness(i);

		System.out.println(Arrays.toString(seats));
		System.out.println(Arrays.toString(un));
	}
}
