package de.uni_augsburg.bazi.monoprop;

import com.google.common.collect.ImmutableList;

import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Rational;

public abstract class MonopropMethod<O extends MonopropMethod.Output>
{
	public final O calculate(Input input)
	{
		return ListCombinations.calculate(input,
				x -> DirectSeats.calculate(x,
						y -> calculateImpl(y)));
	}

	protected abstract O calculateImpl(Input input);


	// ////////////////////////////////////////////////////////////////////////

	public interface Input
	{
		public Int getSeats();
		public ImmutableList<? extends Party> getParties();

		public interface Party
		{
			public String getName();
			public Rational getVotes();
			public default Int getMin()
			{
				return BMath.ZERO;
			};
			public default Int getMax()
			{
				return BMath.INF;
			};
			public default Int getDir()
			{
				return BMath.ZERO;
			};
		}
	}


	// ////////////////////////////////////////////////////////////////////////


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
