package de.uni_augsburg.bazi.common.algorithm;

import de.uni_augsburg.bazi.common.data.Data;
import de.uni_augsburg.bazi.common.data.Default;
import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Real;

import java.util.ArrayList;
import java.util.List;

public interface VectorInput extends Data
{
	@Default("") String name();
	@Default("0") default Int seats() { return BMath.ZERO; }
	default List<? extends Party> parties() { return new ArrayList<>(); }

	public interface Party extends Data
	{
		@Default("") String name();
		@Default("0") default Real votes() { return BMath.ZERO; }
		@Default("0") default Int min() { return BMath.ZERO; }
		@Default("oo") default Int max() { return BMath.INF; }
	}
}
