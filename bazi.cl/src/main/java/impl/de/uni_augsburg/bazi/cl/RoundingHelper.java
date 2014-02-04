package de.uni_augsburg.bazi.cl;

import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Real;
import de.uni_augsburg.bazi.monoprop.RoundingFunction;

import java.util.Arrays;
import java.util.List;

public class RoundingHelper
{
	public static List<String> round(List<Real> numbers, int minDigits)
	{
		return null;
	}

	public static String round(Real number, int minDigits, int maxDigits, RoundingFunction r)
	{
		Int intPart = number.floor();
		List<Real> borders = Arrays.asList(
			r.getBorder(intPart.sub(1), maxDigits),
			r.getBorder(intPart, maxDigits),
			r.getBorder(intPart.add(1), maxDigits)
		);

		if (borders.contains(number)) return number.precision(maxDigits).toString();

		int digits = minDigits;
		while (borders.contains(number.precision(digits)))
			digits++;
		return number.precision(digits).toString();
	}
}
