package de.uni_augsburg.bazi.common.algorithm;

import de.uni_augsburg.bazi.common.util.MList;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Real;

/**
 * Created by Marco on 21.02.14.
 */
public interface VectorOutput extends VectorInput, AlgorithmOutput
{
	public MList<? extends Party> parties();

	public void name(String name);
	public void seats(Int seats);
	public void parties(MList<? extends Party> parties);

	public interface Party extends VectorInput.Party
	{
		public Int seats();
		public Uniqueness uniqueness();

		public void name(String name);
		public void votes(Real votes);
		public void min(Int min);
		public void max(Int max);
		public void seats(Int seats);
		public void uniqueness(Uniqueness uniqueness);
	}
}
