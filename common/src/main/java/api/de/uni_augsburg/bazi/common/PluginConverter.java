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

	@Override public T unpack(Castable castable)
	{
		CastableObject data;
		if (castable.isCastableObject()) data = castable.asCastableObject();
		else if (castable.isCastableString())
		{
			data = new CastableObject();
			data.cast(Plugin.Params.class).name(castable.asCastableString().getValue());
		}
		else data = new CastableObject();
		return PluginManager.tryInstantiate(type, data).orElse(null);
	}
	@Override public Castable pack(T t)
	{
		return new CastableString(t == null ? null : t.toString());
	}
}
