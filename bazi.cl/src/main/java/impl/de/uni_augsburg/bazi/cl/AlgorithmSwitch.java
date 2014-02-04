package de.uni_augsburg.bazi.cl;

import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.monoprop.DivisorMethod;
import de.uni_augsburg.bazi.monoprop.MonopropInput;
import de.uni_augsburg.bazi.monoprop.RoundingFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class AlgorithmSwitch
{
	private static final Logger LOG = LoggerFactory.getLogger(AlgorithmSwitch.class);

	public static void calculate(BaziFile baziFile)
	{
		List<BasicMethod.Output> results = new ArrayList<>();
		BasicMethod method = new BasicMethod.Divisor(new DivisorMethod(RoundingFunction.DIV_STD, 20));

		List<BasicMethod> methods = Arrays.asList(method);
		List<Int> seats = baziFile.seats.stream().map(Interval::values)
			.reduce((x, y) -> {x.addAll(y); return x;}).get();


		BasicMethods.calculate(methods, seats, baziFile.parties);


		//LOG.info(BasicMethod.asStringTable(baziFile.parties, results, Options.Orientation.VERTICAL, Options.DivisorFormat.QUOTIENT, Options.TieFormat.CODED).toString());
	}
}
