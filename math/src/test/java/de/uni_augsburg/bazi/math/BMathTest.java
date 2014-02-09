package de.uni_augsburg.bazi.math;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import de.uni_augsburg.bazi.common.Json;

public class BMathTest
{
	private class Data
	{
		Int[] intadd;
		Int[] intmul;
		Rational[] ratadd;
		Rational[] ratmul;
	}

	@Test public void test() throws IOException
	{
		String json = Resources.toString(Resources.getResource(getClass(), "numbers.txt"), Charsets.UTF_8);
		Data data = Json.fromJson(json, Data.class);

		for (int i = 0; i < data.intadd.length; i += 3)
		{
			Assert.assertTrue(data.intadd[i].add(data.intadd[i + 1]).equals(data.intadd[i + 2]));
			Assert.assertTrue(data.intadd[i + 2].sub(data.intadd[i + 1]).equals(data.intadd[i]));
		}

		for (int i = 0; i < data.intmul.length; i += 3)
		{
			Assert.assertTrue(data.intmul[i].mul(data.intmul[i + 1]).equals(data.intmul[i + 2]));
			Assert.assertTrue(data.intmul[i + 2].div(data.intmul[i + 1]).equals(data.intmul[i]));
		}

		for (int i = 0; i < data.ratadd.length; i += 3)
		{
			Assert.assertTrue(data.ratadd[i].add(data.ratadd[i + 1]).equals(data.ratadd[i + 2]));
			Assert.assertTrue(data.ratadd[i + 2].sub(data.ratadd[i + 1]).equals(data.ratadd[i]));
		}

		for (int i = 0; i < data.intmul.length; i += 3)
		{
			Assert.assertTrue(data.ratmul[i].mul(data.ratmul[i + 1]).equals(data.ratmul[i + 2]));
			if (data.ratmul[i + 1].sgn() != 0)
				Assert.assertTrue(data.ratmul[i + 2].div(data.ratmul[i + 1]).equals(data.ratmul[i]));
		}

		Assert.assertTrue(BMath.ONE.add(BMath.NAN).equals(BMath.NAN));
		Assert.assertTrue(BMath.ONE.sub(BMath.NAN).equals(BMath.NAN));
		Assert.assertTrue(BMath.ONE.mul(BMath.NAN).equals(BMath.NAN));
		Assert.assertTrue(BMath.ONE.div(BMath.NAN).equals(BMath.NAN));
		Assert.assertTrue(BMath.HALF.add(BMath.NAN).equals(BMath.NAN));
		Assert.assertTrue(BMath.HALF.sub(BMath.NAN).equals(BMath.NAN));
		Assert.assertTrue(BMath.HALF.mul(BMath.NAN).equals(BMath.NAN));
		Assert.assertTrue(BMath.HALF.div(BMath.NAN).equals(BMath.NAN));
	}

	// public static void main(String[] args)
	// {
	// Random r = new Random();
	// StringBuilder sb = new StringBuilder();
	// for (int i = 0; i < 1000; i++)
	// {
	// if (sb.length() > 0)
	// sb.append(",");
	// int a = r.nextInt(1000) - 500, b = r.nextInt(1000) - 500, c = r.nextInt(1000) - 500, d = r.nextInt(1000) - 500;
	// if (b == 0)
	// b = 1;
	// if (d == 0)
	// d = 1;
	// BigRational x = new BigRational(a, b), y = new BigRational(c, d), z = x.mul(y);
	// sb.append(String.format("\"%s\",\"%s\",\"%s\"", x, y, z));
	// }
	// System.out.println(sb);
	// }
}
