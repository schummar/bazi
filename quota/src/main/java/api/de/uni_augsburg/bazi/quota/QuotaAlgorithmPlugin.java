package de.uni_augsburg.bazi.quota;

import de.uni_augsburg.bazi.common.Plugin;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by Marco on 05.03.14.
 */
public class QuotaAlgorithmPlugin implements Plugin<QuotaAlgorithm>
{
	@Override public Class<QuotaAlgorithm> getInstanceType()
	{
		return QuotaAlgorithm.class;
	}
	@Override public List<Object> getParamAttributes()
	{
		return Collections.emptyList();
	}
	@Override public Optional<QuotaAlgorithm> tryInstantiate(Params params)
	{
		String name = params.name().toLowerCase();
		if (name.equals("hare")) name = "haqgrr";
		else if (name.equals("droop")) name = "drqgrr";

		if (name.length() != 6) return Optional.empty();
		String qs = name.substring(0, 3);
		String rs = name.substring(3);

		QuotaFunction q;
		switch (qs)
		{
			case "haq":
				q = QuotaFunction.HARE;
				break;
			case "hq1":
				q = QuotaFunction.HARE_VAR1;
				break;
			case "hq2":
				q = QuotaFunction.HARE_VAR2;
				break;

			case "drq":
				q = QuotaFunction.DROOP;
				break;
			case "dq1":
				q = QuotaFunction.DROOP_VAR1;
				break;
			case "dq2":
				q = QuotaFunction.DROOP_VAR2;
				break;
			case "dq3":
				q = QuotaFunction.DROOP_VAR3;
				break;

			default:
				return Optional.empty();
		}

		ResidualHandler r;
		switch (rs)
		{
			case "grr":
				r = ResidualHandler.GREATEST_REMINDERS;
				break;
			case "gr1":
				r = ResidualHandler.GREATEST_REMAINDERS_MIN;
				break;
			case "wta":
				r = ResidualHandler.WINNER_TAKES_ALL;
				break;

			default:
				return Optional.empty();
		}

		return Optional.of(new QuotaAlgorithm(q, r, params.name()));
	}
}
