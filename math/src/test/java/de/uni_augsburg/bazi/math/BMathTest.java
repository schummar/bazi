package de.uni_augsburg.bazi.math;

import de.schummar.castable.Attribute;
import de.uni_augsburg.bazi.json.JsonFormat;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class BMathTest
{
	private interface Data extends de.schummar.castable.Data
	{
		@Attribute List<Int> intadd();
		@Attribute List<Int> intmul();
		@Attribute List<Rational> ratadd();
		@Attribute List<Rational> ratmul();
	}

	@Test public void test() throws IOException
	{
		Data data = new JsonFormat()
			.deserialize(new InputStreamReader(BMathTest.class.getResourceAsStream("numbers.txt")))
			.asCastableObject().cast(Data.class);

		for (int i = 0; i < data.intadd().size(); i += 3)
		{
			Assert.assertTrue(data.intadd().get(i).add(data.intadd().get(i + 1)).equals(data.intadd().get(i + 2)));
			Assert.assertTrue(data.intadd().get(i + 2).sub(data.intadd().get(i + 1)).equals(data.intadd().get(i)));
		}

		for (int i = 0; i < data.intmul().size(); i += 3)
		{
			Assert.assertTrue(data.intmul().get(i).mul(data.intmul().get(i + 1)).equals(data.intmul().get(i + 2)));
			Assert.assertTrue(data.intmul().get(i + 2).div(data.intmul().get(i + 1)).equals(data.intmul().get(i)));
		}

		for (int i = 0; i < data.ratadd().size(); i += 3)
		{
			Assert.assertTrue(data.ratadd().get(i).add(data.ratadd().get(i + 1)).equals(data.ratadd().get(i + 2)));
			Assert.assertTrue(data.ratadd().get(i + 2).sub(data.ratadd().get(i + 1)).equals(data.ratadd().get(i)));
		}

		for (int i = 0; i < data.intmul().size(); i += 3)
		{
			Assert.assertTrue(data.ratmul().get(i).mul(data.ratmul().get(i + 1)).equals(data.ratmul().get(i + 2)));
			if (data.ratmul().get(i + 1).sgn() != 0)
				Assert.assertTrue(data.ratmul().get(i + 2).div(data.ratmul().get(i + 1)).equals(data.ratmul().get(i)));
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
