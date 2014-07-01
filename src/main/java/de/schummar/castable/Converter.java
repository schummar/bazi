package de.schummar.castable;

import java.util.function.Function;

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

	default boolean shallow()
	{
		return getClass().isAnnotationPresent(ConvertShallow.class);
	}

	default Function<String, String> createValidator(T def)
	{
		if (def == null) return Function.identity();
		String defs = pack(def).asCastableString().getValue();
		return s -> {
			if (s != null) try
			{
				T t = unpack(new CastableString(s));
				return pack(t).asCastableString().getValue();
			}
			catch (Exception ignore) {}
			return defs;
		};
	}
}
