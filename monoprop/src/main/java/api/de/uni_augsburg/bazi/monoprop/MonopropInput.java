package de.uni_augsburg.bazi.monoprop;


import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Real;

import java.util.ArrayList;
import java.util.Collection;

public interface MonopropInput
{
	public static MonopropInput create(Int seats, Collection<? extends Party> parties)
	{
		return new MonopropInput()
		{
			@Override
			public Int seats() { return seats; }
			@Override
			public Collection<? extends Party> parties() { return parties; }
		};
	}


	public Int seats();
	public Collection<? extends Party> parties();

	public interface Party
	{
		public String name();
		public Real votes();
		public default Int min() { return BMath.ZERO; }
		public default Int max() { return BMath.INF; }
		public default Int dir() { return BMath.ZERO; }
		public default Collection<? extends Party> parties() { return new ArrayList<>(); }
	}
}
