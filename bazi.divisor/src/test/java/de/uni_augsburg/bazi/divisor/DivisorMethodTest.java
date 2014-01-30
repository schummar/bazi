package de.uni_augsburg.bazi.divisor;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Resources;

import de.uni_augsburg.bazi.common.Json;
import de.uni_augsburg.bazi.common.Version;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Rational;
import de.uni_augsburg.bazi.monoprop.MonopropMethod;

public class DivisorMethodTest
{
	private static final Logger log = Logger.getLogger(DivisorMethodTest.class);

	public static void main(String[] args) throws IOException
	{
		BasicConfigurator.configure();
		log.debug("version: " + Version.getCurrentVersionName());
		log.debug("begin");

		String json = Resources.toString(Resources.getResource(DivisorMethodTest.class, "input_divisor.bazi"), Charsets.UTF_8);
		log.debug("warnings: " + Json.checkJson(json, Input.class));

		MonopropMethod.Input input = Json.fromJson(json, Input.class);
		MonopropMethod qm = new DivisorMethod(RoundingFunction.DIV_GEO, 10);

		log.debug("start calculation");
		long t = System.nanoTime();
		MonopropMethod.Output output = qm.calculate(input);
		t = System.nanoTime() - t;
		log.debug(String.format("end calculation (%s ms)", t / 1000.0 / 1000.0));


		log.debug("output: " + Json.toJson(output));
		log.debug("end");
	}

	public static class Input implements MonopropMethod.Input
	{
		public Int seats;
		public List<DivisorMethodTest.Party> parties;

		public Input(Int seats, List<DivisorMethodTest.Party> parties)
		{
			super();
			this.seats = seats;
			this.parties = parties;
		}
		@Override public Int seats()
		{
			return seats;
		}
		@Override public ImmutableList<? extends Party> parties()
		{
			return ImmutableList.copyOf(parties);
		}
	}

	public static class Party implements MonopropMethod.Input.Party
	{
		public String name;
		public Rational votes;
		public Int min, max, dir;

		public Party(String name, Rational votes, Int min, Int max, Int dir)
		{
			super();
			this.name = name;
			this.votes = votes;
			this.min = min;
			this.max = max;
			this.dir = dir;
		}
		@Override public String name()
		{
			return name;
		}
		@Override public Rational votes()
		{
			return votes;
		}
		@Override public Int min()
		{
			return min;
		}
		@Override public Int max()
		{
			return max;
		}
		@Override public Int dir()
		{
			return dir;
		}
	}
}
