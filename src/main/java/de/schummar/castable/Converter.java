package de.schummar.castable;

public interface Converter<T>
{
	T unpack(Castable o);
	default T tryUnpack(Castable o)
	{
		try
		{
			return unpack(o);
		}
		catch (Exception ignore) {}
		return null;
	}


	Castable pack(T t);
	default Castable tryPack(T t)
	{
		try
		{
			return pack(t);
		}
		catch (Exception ignore) {}
		return new CastableUninitialized();
	}
}
