package de.uni_augsburg.bazi.monoprop;

import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Rational;

public class DummyParty implements MonopropMethod.Input.Party
{
	public String name;
	public Rational votes;
	public Int min, max, dir;

	public DummyParty(String name, Rational votes, Int min, Int max, Int dir)
	{
		this.name = name;
		this.votes = votes;
		this.min = min;
		this.max = max;
		this.dir = dir;
	}
	@Override public String getName()
	{
		return name;
	}
	@Override public Rational getVotes()
	{
		return votes;
	}
	@Override public Int getMin()
	{
		return min;
	}
	@Override public Int getMax()
	{
		return max;
	}
	@Override public Int getDir()
	{
		return dir;
	}
}
