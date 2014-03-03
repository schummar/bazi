package de.uni_augsburg.bazi.common.algorithm;

import de.uni_augsburg.bazi.common.data.Data;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Real;

import java.util.List;

/**
 * Created by Marco on 21.02.14.
 */
public interface VectorInput extends Data
{
	String name();
	void name(String name);

	Int seats();
	void seats(Int seats);

	List<? extends Party> parties();

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
