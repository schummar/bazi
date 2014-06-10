package de.uni_augsburg.bazi.common;

import de.schummar.castable.Castable;
import de.schummar.castable.CastableObject;
import de.schummar.castable.CastableString;
import de.schummar.castable.Converter;

public class PluginConverter<T extends Plugin.Instance> implements Converter<T>
{
	private final Class<T> type;

	public PluginConverter(Class<T> type)
	{
		this.type = type;
	}

	@Override public T apply(Castable castable)
	{
		CastableObject data;
		if (castable instanceof CastableObject) data = castable.asCastableObject();
		else if (castable instanceof CastableString)
		{
			data = new CastableObject();
			data.cast(Plugin.Params.class).name(castable.asCastableString().getValue());
		}
		else data = new CastableObject();
		return PluginManager.tryInstantiate(type, data).orElse(null);
	}
	@Override public Castable applyInverse(T t)
	{
		if (t == null) return new CastableString();
		return new CastableString(t.toString());
	}
}
