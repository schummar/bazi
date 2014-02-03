package de.uni_augsburg.bazi.cl;

enum Language
{
	EN("english"), DE("deutsch"), IT("italiano"), ES("español");

	private String name;

	Language(String name)
	{
		this.name = name;
	}

	public String getName() { return name; }
}
