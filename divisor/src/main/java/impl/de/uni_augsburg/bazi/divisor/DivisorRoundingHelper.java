package de.uni_augsburg.bazi.divisor;

import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Real;

import java.util.Arrays;
import java.util.List;

public class DivisorRoundingHelper
{
	public static Real round(Real q, int minDigits, int maxDigits, RoundingFunction r)
	{
		Int intPart = q.floor();
		List<Real> borders = Arrays.asList(
			r.getBorder(intPart.sub(1), maxDigits),
			r.getBorder(intPart, maxDigits),
			r.getBorder(intPart.add(1), maxDigits)
		);

		if (borders.contains(q)) return q.precision(maxDigits);

		int digits = minDigits - 1;
		Real qr;
		do
		{
			digits++;
			qr = q.round(digits);
		} while (borders.contains(qr) && digits < maxDigits);

		return qr.precision(digits);
	}
}
