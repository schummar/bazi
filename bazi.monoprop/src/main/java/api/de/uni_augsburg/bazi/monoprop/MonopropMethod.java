package de.uni_augsburg.bazi.monoprop;

import de.uni_augsburg.bazi.common.DataProxy;
import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Rational;

import java.util.List;

public abstract class MonopropMethod<O extends MonopropMethod.Output>
{
	public final O calculate(Input input)
	{
		return ListCombinations.calculate(
			input,
			x -> DirectSeats.calculate(
				x,
				this::calculateImpl
			)
		);
	}

	protected abstract O calculateImpl(Input input);


	// ////////////////////////////////////////////////////////////////////////

	public interface Input
	{
		public static Input create(Int seats, List<? extends Party> parties)
		{ return DataProxy.of(Input.class).keys("seats", "parties").create(seats, parties); }

		public Int seats();
		public List<? extends Party> parties();

		public interface Party
		{
			public static Party create(String name, Rational votes, Int min, Int max, Int dir)
			{return DataProxy.of(Party.class).keys("name", "votes", "min", "max", "dir").create(name, votes, min, max, dir);}
			public String name();
			public Rational votes();
			public default Int min() { return BMath.ZERO; }
			public default Int max() { return BMath.INF; }
			public default Int dir() { return BMath.ZERO; }
		}
	}


	// ////////////////////////////////////////////////////////////////////////


	public interface Output extends Input
	{
		public static Output create(Int seats, List<? extends Party> parties)
		{return DataProxy.of(Output.class).extend(Input.class).create(Input.create(seats, parties));}

		@Override
		public List<? extends Party> parties();

		public interface Party extends Input.Party
		{
			public static Party create(String name, Rational votes, Int min, Int max, Int dir, Int seats, Uniqueness uniqueness, Rational quotient)
			{return DataProxy.of(Party.class).extend(Input.Party.class).keys("seats", "uniqueness", "quotient").create(Input.Party.create(name, votes, min, max, dir), seats, uniqueness, quotient);}

			public Int seats();
			public Uniqueness uniqueness();
			public Rational quotient();
		}
	}
}
