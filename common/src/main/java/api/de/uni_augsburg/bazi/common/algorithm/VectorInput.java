package de.uni_augsburg.bazi.common.algorithm;

import de.uni_augsburg.bazi.common.Data;
import de.uni_augsburg.bazi.common.util.MList;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Real;

/**
 * Created by Marco on 21.02.14.
 */
public interface VectorInput extends Data
{
	String name();
	void name(String name);

	Int seats();
	void seats(Int seats);

	MList<? extends Party> parties();

	public interface Party extends Data
	{
		String name();
		void name(String name);

		Real votes();
		void votes(Real votes);

		Int min();
		void min(Int min);

		Int max();
		void max(Int max);
	}
}
