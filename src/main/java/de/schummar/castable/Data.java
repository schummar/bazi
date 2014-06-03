package de.schummar.castable;

public interface Data
{
	static <T extends Data> T create(Class<T> c) { return new CastableObject().cast(c); }


	CastableObject src();
	default Data copy() { return src().copy(); }
	default <T extends Data> T cast(Class<T> c) { return src().cast(c); }
	default void merge(Data that) { src().merge(that); }
}
