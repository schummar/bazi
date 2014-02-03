package de.uni_augsburg.bazi.cl;

import de.uni_augsburg.bazi.common.Json;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.monoprop.DivisorMethod;
import de.uni_augsburg.bazi.monoprop.MonopropOutput;
import de.uni_augsburg.bazi.monoprop.RoundingFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

class AlgorithmSwitch
{
	private static final Logger LOG = LoggerFactory.getLogger(AlgorithmSwitch.class);

	public static void calculate(BaziFile baziFile)
	{
		List<MonopropOutput> results = new ArrayList<>();
		DivisorMethod method = new DivisorMethod(RoundingFunction.DIV_STD);

		for (Interval interval : baziFile.seats)
			for (Int seats : interval)
				results.add(method.calculate(new Input(seats, baziFile.parties)));

		for (MonopropOutput output : results)
			LOG.info(Json.toJson(output));
	}
}
