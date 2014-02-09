package de.uni_augsburg.bazi.cl;

import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.monoprop.DivisorMethod;
import de.uni_augsburg.bazi.monoprop.RoundingFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

class AlgorithmSwitch
{
	private static final Logger LOG = LoggerFactory.getLogger(AlgorithmSwitch.class);

	public static String calculate(BaziFile baziFile, Format format)
	{
		BasicMethod method = new BasicMethod.Divisor(new DivisorMethod(RoundingFunction.DIV_STD, 20));

		List<BasicMethod> methods = Arrays.asList(method);
		List<Int> seats = baziFile.seats.stream().map(Interval::values)
			.reduce((x, y) -> {x.addAll(y); return x;}).get();

		BasicMethods.OutputPackage op = BasicMethods.calculate(
			methods,
			seats,
			baziFile.parties,
			new Options(Options.Orientation.VERTICAL, Options.DivisorFormat.DIV_INTERVAL, Options.TieFormat.CODED)
		);
		return format.serialize(op);
	}
}
