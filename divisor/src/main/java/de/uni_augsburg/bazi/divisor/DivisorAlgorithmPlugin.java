package de.uni_augsburg.bazi.divisor;


import de.schummar.castable.Attribute;
import de.uni_augsburg.bazi.common.Plugin;
import de.uni_augsburg.bazi.common.Resources;
import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Rational;
import javafx.beans.property.Property;

import java.util.*;

/** This plugin produces instances of DivisorAlgorithm. */
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
		String name = null;
		if (params.name().matches(DIV_STD))
		{
			r = RoundingFunction.DIV_STD;
			name = Resources.get("method.divstd");
		}
		else if (params.name().matches(DIV_UPW))
		{
			r = RoundingFunction.DIV_UPW;
			name = Resources.get("method.divdwn");
		}
		else if (params.name().matches(DIV_DWN))
		{
			r = RoundingFunction.DIV_DWN;
			name = Resources.get("method.divupw");
		}
		else if (params.name().matches(DIV_HAR))
		{
			r = RoundingFunction.DIV_HAR;
			name = Resources.get("method.divhar");
		}
		else if (params.name().matches(DIV_GEO))
		{
			r = RoundingFunction.DIV_GEO;
			name = Resources.get("method.divgeo");
		}
		else if (params.name().matches(DIV_STA))
		{
			r = buildStationary(cast.r());
			name = Resources.get("method.divsta");
		}
		else if (params.name().matches(DIV_POW))
		{
			r = buildPower(cast.p());
			name = Resources.get("method.divpow");
		}
		if (r == null) return Optional.empty();
		return Optional.of(new DivisorAlgorithm(r, name));
	}

	public static RoundingFunction.Stationary buildStationary(String line)
	{
		String[] split = line.trim().split("[,|;|\\s]+", 2);
		Rational r = Rational.valueOf(split[0]);
		Map<Int, Rational> specialCases = split.length > 1 ? buildSpecialCases(split[1]) : null;
		if (r.equals(BMath.ZERO) && specialCases == null) return RoundingFunction.DIV_UPW;
		if (r.equals(BMath.HALF) && specialCases == null) return RoundingFunction.DIV_STD;
		if (r.equals(BMath.ONE) && specialCases == null) return RoundingFunction.DIV_DWN;
		return new RoundingFunction.Stationary(r, specialCases);
	}

	public static RoundingFunction.Power buildPower(String line)
	{
		String[] split = line.trim().split("[,|;|\\s]+", 2);
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


	/** Parameters for a DivisorAlgorithm. */
	public interface Params extends Plugin.Params
	{
		/**
		 * The parameter p for power rounding.
		 * @return the parameter p for power rounding.
		 */
		@Attribute(def = "") Property<String> pProperty();
		default String p() { return pProperty().getValue(); }
		default void p(String v) { pProperty().setValue(v); }

		/**
		 * The parameter r for stationary rounding.
		 * @return the parameter r for stationary rounding.
		 */
		@Attribute(def = "") Property<String> rProperty();
		default String r() { return rProperty().getValue(); }
		default void r(String v) { rProperty().setValue(v); }
	}
}
