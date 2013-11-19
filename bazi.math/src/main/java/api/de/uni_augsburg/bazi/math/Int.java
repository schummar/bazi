package de.uni_augsburg.bazi.math;

import java.math.BigInteger;

public interface Int extends Rational
{
	public BigInteger getValue();

	public Int add(Int that);
	@Override public Int add(long that);

	public Int sub(Int that);
	@Override public Int sub(long that);

	public Int mul(Int that);
	@Override public Int mul(long that);

	@Override public Int pow(Int that);
	@Override public Int pow(long that);

	public Int min(Int that);
	@Override public Int min(long that);

	public Int max(Int that);
	@Override public Int max(long that);

	public boolean equals(Int that);
	@Override public boolean equals(String that);
	@Override public boolean equals(long that);

	public int compare(Int that);
	@Override public int compare(String that);
	@Override public int compare(long that);

	@Override public Int neg();
	@Override public int sgn();
}
