package de.uni_augsburg.bazi.quota;

import de.uni_augsburg.bazi.math.Interval;
import de.uni_augsburg.bazi.math.Real;

/**
 * Created by Marco on 07.03.14.
 */
public class QuotaRoundingHelper
{
	public static Real round(Real q, Interval split, int minDigits, int maxDigits)
	{
		Real f = q.frac();
		if (split.contains(f)) return q.precision(maxDigits);

		int comp = split.compareTo(f);
		int digits = minDigits - 1;
		Real fr;
		do
		{
			digits++;
			fr = f.precision(digits);
		} while (split.compareTo(fr) != comp && digits < maxDigits);

		return q.floor().add(fr);
	}
}
