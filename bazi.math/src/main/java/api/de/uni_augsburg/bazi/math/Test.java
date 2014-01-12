package de.uni_augsburg.bazi.math;


public class Test
{
	public static void main(String[] args)
	{
		AReal a = new AInt(1), b = new AInt(3);
		System.out.println(a.div(b).getClass());
		Runnable r = () -> System.out.println("Yeah");
		r.run();
	}
}
