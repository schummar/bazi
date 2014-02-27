package de.uni_augsburg.bazi.common.algorithm;

import de.uni_augsburg.bazi.common.util.MList;
import de.uni_augsburg.bazi.math.Int;

/**
 * Created by Marco on 21.02.14.
 */
public interface VectorOutput extends VectorInput, AlgorithmOutput
{
	MList<? extends Party> parties();

	public interface Party extends VectorInput.Party
	{
		Int seats();
		void seats(Int seats);

		Uniqueness uniqueness();
		void uniqueness(Uniqueness uniqueness);
	}
}
