package de.schummar.castable;

public interface Converter<T>
{
	T apply(Castable o);
	Castable applyInverse(T t);
}
