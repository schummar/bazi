package de.uni_augsburg.bazi.monoprop;

import com.google.common.collect.ImmutableList;

import de.uni_augsburg.bazi.common.Json.Adapter;
import de.uni_augsburg.bazi.common.Json.JsonAdapter;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Rational;

public interface MonopropMethod
{
	public Output calculate(Input input);


	// ////////////////////////////////////////////////////////////////////////


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

			@JsonAdapter public static final Adapter<Party> ADAPTER = new Adapter<Party>()
			{
				@Override public Class<? extends Party> getPlainType()
				{
					return de.uni_augsburg.bazi.monoprop.Input.Party.class;
				}
			};
		}

		@JsonAdapter public static final Adapter<Input> ADAPTER = new Adapter<Input>()
		{
			@Override public Class<? extends Input> getPlainType()
			{
				return de.uni_augsburg.bazi.monoprop.Input.class;
			}
		};
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

			@JsonAdapter public static final Adapter<Party> ADAPTER = new Adapter<Party>()
			{
				@Override public Class<? extends Party> getPlainType()
				{
					return de.uni_augsburg.bazi.monoprop.Output.Party.class;
				}
			};
		}

		@JsonAdapter public static final Adapter<Output> ADAPTER = new Adapter<Output>()
		{
			@Override public Class<? extends Output> getPlainType()
			{
				return de.uni_augsburg.bazi.monoprop.Output.class;
			}
		};

	}
}
