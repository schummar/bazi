package de.uni_augsburg.bazi.common.algorithm;

import de.uni_augsburg.bazi.common.data.Data;
import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Real;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marco on 21.02.14.
 */
public interface VectorInput extends Data
{
	String name();
	default Int seats() { return BMath.ZERO; }
	default List<? extends Party> parties() { return new ArrayList<>(); }

	public interface Party extends Data
	{
		String name();
		default Real votes() { return BMath.ZERO; }
		default Int min() { return BMath.ZERO; }
		default Int max() { return BMath.INF; }
	}
}
