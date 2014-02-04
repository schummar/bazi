package de.uni_augsburg.bazi.monoprop;


import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Rational;

import java.util.ArrayList;
import java.util.List;

public interface MonopropInput
{
	public Int seats();
	public List<? extends Party> parties();

	public interface Party
	{
		public Object id();
		public String name();
		public Rational votes();
		public default Int min() { return BMath.ZERO; }
		public default Int max() { return BMath.INF; }
		public default Int dir() { return BMath.ZERO; }
		public default List<? extends Party> parties() { return new ArrayList<>(); }
	}
}