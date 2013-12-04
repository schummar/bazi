package de.uni_augsburg.bazi.monoprop;

import java.io.IOException;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import de.uni_augsburg.bazi.common.Json;

public class QuotaMethodTest
{
	private static final Logger log = Logger.getLogger(QuotaMethodTest.class);

	public static void main(String[] args) throws IOException, NoSuchMethodException, SecurityException
	{
		BasicConfigurator.configure();
		log.debug("begin");

		String json = Resources.toString(Resources.getResource(QuotaMethodTest.class, "test_input.bazi"), Charsets.UTF_8);
		log.debug("warnings: " + Json.checkJson(json, MonopropMethod.Input.class));

		MonopropMethod.Input input = Json.fromJson(json, MonopropMethod.Input.class);
		MonopropMethod qm = new QuotaMethod(QuotaFunction.HARE, ResidualHandler.GREATEST_REMINDERS);

		log.debug("start calculation");
		long t = System.nanoTime();
		MonopropMethod.Output output = qm.calculate(input);
		t = System.nanoTime() - t;
		log.debug(String.format("end calculation (%s ms)", t / 1000.0 / 1000.0));


		log.debug("output: " + Json.toJson(output));
		log.debug("end");

		// Int allSeats = BMath.intOf(4);
		// final Int[] seats = { BMath.ZERO, BMath.ZERO, BMath.ZERO };
		// final Int[] min = { BMath.intOf(2), BMath.intOf(0), BMath.intOf(0) };
		// final Rational[] weights = { BMath.rationalOf("1"), BMath.rationalOf("1"), BMath.rationalOf("1") };
		// final RoundingFunction.Stationary rf = new RoundingFunction.Stationary(BMath.HALF, null);
		//
		//
		// PriorityQueue pq = new PriorityQueue(seats.length)
		// {
		// @Override public Real increaseValue(int i)
		// {
		// return weights[i].div(rf.getBorder(seats[i]));
		// }
		// @Override public Real decreaseValue(int i)
		// {
		// return weights[i].div(rf.getBorder(seats[i].sub(1)));
		// }
		// @Override public int increasePreference(int i)
		// {
		// if (seats[i].compareTo(min[i]) < 0)
		// return 1;
		// return 0;
		// }
		// @Override public int decreasePreference(int i)
		// {
		// if (seats[i].compareTo(min[i]) <= 0)
		// return -1;
		// return 0;
		// }
		// };
		//
		//
		// for (Int sum = BMath.ZERO; sum.compareTo(allSeats) < 0; sum = sum.add(1))
		// {
		// int i = pq.nextIncrease();
		// seats[i] = seats[i].add(1);
		// }
		// Uniqueness[] un = new Uniqueness[seats.length];
		// for (int i = 0; i < un.length; i++)
		// un[i] = pq.getUniqueness(i);
		//
		// System.out.println(Arrays.toString(seats));
		// System.out.println(Arrays.toString(un));
	}
}
