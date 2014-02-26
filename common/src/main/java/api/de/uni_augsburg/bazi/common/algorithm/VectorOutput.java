package de.uni_augsburg.bazi.common.algorithm;

import de.uni_augsburg.bazi.common.util.MList;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Real;

/**
 * Created by Marco on 21.02.14.
 */
public interface VectorOutput extends VectorInput, AlgorithmOutput
{
	MList<? extends Party> parties();

	void name(String name);
	void seats(Int seats);
	void parties(MList<? extends Party> parties);

	public interface Party extends VectorInput.Party
	{
		Int seats();
		Uniqueness uniqueness();

		void name(String name);
		void votes(Real votes);
		void min(Int min);
		void max(Int max);
		void seats(Int seats);
		void uniqueness(Uniqueness uniqueness);
	}
}
