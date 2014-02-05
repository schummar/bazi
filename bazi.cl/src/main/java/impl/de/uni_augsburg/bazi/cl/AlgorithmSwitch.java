package de.uni_augsburg.bazi.cl;

import de.uni_augsburg.bazi.common.Json;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.monoprop.DivisorMethod;
import de.uni_augsburg.bazi.monoprop.RoundingFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class AlgorithmSwitch
{
	private static final Logger LOG = LoggerFactory.getLogger(AlgorithmSwitch.class);

	public static void calculate(BaziFile baziFile, Format format)
	{
		List<BasicMethod.Output> results = new ArrayList<>();
		BasicMethod method = new BasicMethod.Divisor(new DivisorMethod(RoundingFunction.DIV_STD, 20));

		List<BasicMethod> methods = Arrays.asList(method);
		List<Int> seats = baziFile.seats.stream().map(Interval::values)
			.reduce((x, y) -> {x.addAll(y); return x;}).get();


		BasicMethods.OutputPackage op = BasicMethods.calculate(methods, seats, baziFile.parties);

		if (format == Format.JSON)
			LOG.info(Json.toJson(op.getAll().stream().map(BasicMethod.Output::output).collect(Collectors.toList())));

		else
			op.asStringTables(Options.Orientation.VERTICAL, Options.DivisorFormat.DIV_INTERVAL, Options.TieFormat.CODED).stream()
				.map(Object::toString)
				.forEach(LOG::info);
	}
}
