package de.uni_augsburg.bazi.monoprop;

import com.google.common.collect.ImmutableList;

import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Rational;

public interface MonopropMethod
{
	public Output calculate(Input input);

	public interface Input
	{
		public Int getSeats();
		public ImmutableList<? extends Party> getParties();

		public interface Party
		{
			public String getName();
			public Rational getVotes();
			public Int getMin();
			public Int getMax();
			public Int getDir();
		}
	}

	public interface Output extends Input
	{
		@Override public ImmutableList<? extends Party> getParties();

		public interface Party extends Input.Party
		{
			public Int getSeats();
			public Uniqueness getUniqueness();
			public Rational getQuotient();
		}
	}
}
