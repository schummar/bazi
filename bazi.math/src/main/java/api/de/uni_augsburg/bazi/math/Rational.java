package de.uni_augsburg.bazi.math;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import de.uni_augsburg.bazi.common.Json.Adapter;
import de.uni_augsburg.bazi.common.Json.JsonAdapter;

public interface Rational extends Real
{
	public Int getNumerator();
	public Int getDenominator();

	public Rational add(Rational that);
	@Override public Rational add(String that);
	@Override public Rational add(long that);

	public Rational sub(Rational that);
	@Override public Rational sub(String that);
	@Override public Rational sub(long that);

	public Rational mul(Rational that);
	@Override public Rational mul(String that);
	@Override public Rational mul(long that);

	public Rational div(Rational that);
	@Override public Rational div(String that);
	@Override public Rational div(long that);

	public Rational pow(Int that);
	@Override public Rational pow(long that);

	public Rational min(Rational that);
	@Override public Rational min(String that);
	@Override public Rational min(long that);

	public Rational max(Rational that);
	@Override public Rational max(String that);
	@Override public Rational max(long that);

	public boolean equals(Rational that);
	@Override public boolean equals(String that);
	@Override public boolean equals(long that);

	public int compareTo(Rational that);
	@Override public int compareTo(String that);
	@Override public int compareTo(long that);

	@Override public Rational neg();
	@Override public Rational inv();
	@Override public int sgn();

	@Override public Rational frac();


	@JsonAdapter public static final Adapter<Rational> ADAPTER = new Adapter<Rational>()
	{
		@Override public Rational read(JsonReader in) throws java.io.IOException
		{
			return new BRational(in.nextString());
		}
		@Override public void write(JsonWriter out, Rational value) throws java.io.IOException
		{
			out.value(value.toString());
		}
	};
}
