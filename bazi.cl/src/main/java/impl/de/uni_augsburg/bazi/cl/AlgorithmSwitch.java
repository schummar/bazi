package de.uni_augsburg.bazi.cl;

import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.monoprop.DivisorMethod;
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
		List<BasicMethod.Output> results = new ArrayList<>();
		BasicMethod method = new BasicMethod.Divisor(new DivisorMethod(RoundingFunction.DIV_STD, 20));

		for (Interval interval : baziFile.seats)
			for (Int seats : interval)
				results.add(method.calculate(new Input(seats, baziFile.parties)));


		//LOG.info(BasicMethod.asStringTable(baziFile.parties, results, Options.Orientation.VERTICAL, Options.DivisorFormat.QUOTIENT, Options.TieFormat.CODED).toString());
	}
}
