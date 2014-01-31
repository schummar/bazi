package de.uni_augsburg.bazi.monoprop;

import java.io.IOException;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import de.uni_augsburg.bazi.common.Json;
import de.uni_augsburg.bazi.common.Version;

public class QuotaMethodTest
{
	private static final Logger log = Logger.getLogger(QuotaMethodTest.class);

	public static void main(String[] args) throws IOException, NoSuchMethodException, SecurityException
	{
		BasicConfigurator.configure();
		log.debug("version: " + Version.getCurrentVersionName());
		log.debug("begin");

		String json = Resources.toString(Resources.getResource(QuotaMethodTest.class, "input_quota.bazi"), Charsets.UTF_8);
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
	}
}
