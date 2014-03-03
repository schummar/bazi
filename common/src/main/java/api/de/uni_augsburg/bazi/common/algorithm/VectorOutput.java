package de.uni_augsburg.bazi.common.algorithm;

import de.uni_augsburg.bazi.math.Int;

import java.util.List;

/**
 * Created by Marco on 21.02.14.
 */
public interface VectorOutput extends VectorInput, AlgorithmOutput
{
	List<? extends Party> parties();

	public interface Party extends VectorInput.Party
	{
		Int seats();
		void seats(Int seats);

		Uniqueness uniqueness();
		void uniqueness(Uniqueness uniqueness);
	}
}
