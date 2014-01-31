package de.uni_augsburg.bazi.cl;

import de.uni_augsburg.bazi.common.Json;
import de.uni_augsburg.bazi.monoprop.DivisorMethod;
import de.uni_augsburg.bazi.monoprop.RoundingFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlgorithmSwitch
{
	private static final Logger LOG = LoggerFactory.getLogger(AlgorithmSwitch.class);

	public static void calculate(BaziFile baziFile)
	{
		LOG.info(Json.toJson(baziFile));
		DivisorMethod method = new DivisorMethod(RoundingFunction.DIV_STD);
		//DivisorOutput output = method.calculate(baziFile);

		//LOG.info(Json.toJson(output));
	}
}
