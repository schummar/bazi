package de.uni_augsburg.bazi.divisor;


import de.uni_augsburg.bazi.common.Plugin;
import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Rational;

import java.util.*;

public class DivisorAlgorithmPlugin implements Plugin<DivisorAlgorithm>
{
	private static final String
		DIV_STD = "divstd",
		DIV_UPW = "divupw|divup|divauf",
		DIV_DWN = "divdwn|divabr|divab",
		DIV_HAR = "divhar",
		DIV_GEO = "givgeo",
		DIV_STA = "divsta",
		DIV_POW = "divpow";


	@Override public Class<? extends DivisorAlgorithm> getInstanceType() { return DivisorAlgorithm.class; }
	@Override public List<Object> getParamAttributes() { return Collections.emptyList(); }

	@Override public Optional<? extends DivisorAlgorithm> tryInstantiate(Plugin.Params params)
	{
		Params cast = params.cast(Params.class);
		RoundingFunction r = null;
		if (params.name().matches(DIV_STD)) r = RoundingFunction.DIV_STD;
		else if (params.name().matches(DIV_UPW)) r = RoundingFunction.DIV_UPW;
		else if (params.name().matches(DIV_DWN)) r = RoundingFunction.DIV_DWN;
		else if (params.name().matches(DIV_HAR)) r = RoundingFunction.DIV_HAR;
		else if (params.name().matches(DIV_GEO)) r = RoundingFunction.DIV_GEO;
		else if (params.name().matches(DIV_STA)) r = buildStationary(cast.r());
		else if (params.name().matches(DIV_POW)) r = buildPower(cast.p());
		if (r == null) return Optional.empty();
		return Optional.of(new DivisorAlgorithm(r, cast.minPrecision()));
	}

	public static RoundingFunction.Stationary buildStationary(String line)
	{
		String[] split = line.split("[,|;|\\s]+", 2);
		Rational r = Rational.valueOf(split[0]);
		Map<Int, Rational> specialCases = split.length > 1 ? buildSpecialCases(split[1]) : null;
		if (r.equals(BMath.ZERO) && specialCases == null) return RoundingFunction.DIV_UPW;
		if (r.equals(BMath.HALF) && specialCases == null) return RoundingFunction.DIV_STD;
		if (r.equals(BMath.ONE) && specialCases == null) return RoundingFunction.DIV_DWN;
		return new RoundingFunction.Stationary(r, specialCases);
	}

	public static RoundingFunction.Power buildPower(String line)
	{
		String[] split = line.split("[,|;|\\s]+", 2);
		Rational p = Rational.valueOf(split[0]);
		Map<Int, Rational> specialCases = split.length > 1 ? buildSpecialCases(split[1]) : null;
		if (p.equals(BMath.ZERO) && specialCases == null) return RoundingFunction.DIV_GEO;
		if (p.equals(BMath.MINUS_ONE) && specialCases == null) return RoundingFunction.DIV_HAR;
		return new RoundingFunction.Power(p, specialCases);
	}

	public static Map<Int, Rational> buildSpecialCases(String line)
	{
		Map<Int, Rational> map = new HashMap<>();
		for (String s : line.split("[,|;|\\s]+"))
		{
			s = s.trim();
			boolean lowerInterval = s.endsWith("]");
			s = s.replaceAll("\\[", "").replaceAll("\\]", "");
			Rational param = Rational.valueOf(s);
			Int key = lowerInterval ? param.floor().sub(1) : param.floor();
			map.put(key, param.sub(key));
		}
		return map;
	}


	public interface Params extends Plugin.Params
	{
		String p();
		String r();
		int minPrecision();
	}
}