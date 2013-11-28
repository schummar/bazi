package de.uni_augsburg.bazi.monoprop;

import java.io.IOException;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import de.uni_augsburg.bazi.common.Json;

public class QuotaMethodTest
{
	public static void main(String[] args) throws IOException
	{
		String json = Resources.toString(Resources.getResource(QuotaMethodTest.class, "test_input.bazi"), Charsets.UTF_8);
		System.out.println("Warnings: " + Json.checkJson(json, DummyInput.class));

		MonopropMethod.Input input = Json.fromJson(json, DummyInput.class);
		MonopropMethod qm = new QuotaMethod(QuotaFunction.HARE, ResidualHandler.GREATEST_REMINDERS);
		MonopropMethod.Output output = qm.calculate(input);

		System.out.println(Json.toJson(output));
	}
}
