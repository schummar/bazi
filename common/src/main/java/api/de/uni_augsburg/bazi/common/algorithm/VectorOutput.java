package de.uni_augsburg.bazi.common.algorithm;

import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Real;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marco on 21.02.14.
 */
public interface VectorOutput extends VectorInput, AlgorithmOutput
{
	@Override default List<? extends Party> parties() { return new ArrayList<>(); }

	void name(String name);
	void seats(Int seats);
	void parties(List<? extends Party> parties);


	public interface Party extends VectorInput.Party
	{
		default Int seats() { return BMath.ZERO; }
		default Uniqueness uniqueness() { return Uniqueness.UNIQUE; }

		void name(String name);
		void votes(Real votes);
		void min(Int min);
		void max(Int max);
		void seats(Int seats);
		void uniqueness(Uniqueness uniqueness);
	}
}
