package de.uni_augsburg.bazi.math;

public interface Real
{
	public Rational getLo();
	public Rational getHi();

	public Real add(Real that);
	public Real add(String that);
	public Real add(long that);

	public Real sub(Real that);
	public Real sub(String that);
	public Real sub(long that);

	public Real mul(Real that);
	public Real mul(String that);
	public Real mul(long that);

	public Real div(Real that);
	public Real div(String that);
	public Real div(long that);

	public Real pow(Real that);
	public Real pow(String that);
	public Real pow(long that);

	public Real min(Real that);
	public Real min(String that);
	public Real min(long that);

	public Real max(Real that);
	public Real max(String that);
	public Real max(long that);

	public boolean equals(Real that);
	public boolean equals(String that);
	public boolean equals(long that);

	public int compareTo(Real that);
	public int compareTo(String that);
	public int compareTo(long that);

	public Real neg();
	public Real inv();
	public int sgn();


	public String toString(int precision);

	public static class Generator
	{
		public static Real get(String s)
		{
			return new BReal(s);
		}
	}
}
