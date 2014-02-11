package de.uni_augsburg.bazi.cl;

import de.uni_augsburg.bazi.biprop.ASMethod;
import de.uni_augsburg.bazi.biprop.BipropMethod;
import de.uni_augsburg.bazi.biprop.BipropOutput;
import de.uni_augsburg.bazi.biprop.DivisorUpdateFunction;
import de.uni_augsburg.bazi.common.Json;
import de.uni_augsburg.bazi.monoprop.DivisorMethod;
import de.uni_augsburg.bazi.monoprop.RoundingFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class AlgorithmSwitch
{
	private static final Logger LOG = LoggerFactory.getLogger(AlgorithmSwitch.class);

	public static String calculate(BaziFile baziFile, Format format)
	{
		/*BasicMethod method = new BasicMethod.Divisor(new DivisorMethod(RoundingFunction.DIV_STD, 20));
		baziFile.methods = Arrays.asList(method);
		BasicMethods.OutputPackage op = BasicMethods.calculate(baziFile);
		return format.serialize(op);*/

		DivisorMethod divisorMethod = new DivisorMethod(RoundingFunction.DIV_STD);
		BipropMethod method = new ASMethod(divisorMethod, DivisorUpdateFunction.MIDPOINT);
		BipropOutput output = method.calculate(baziFile);
		return Json.toJson(output);
	}
}
