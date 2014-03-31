package de.uni_augsburg.bazi.common.format;

import de.uni_augsburg.bazi.common.Plugin;
import de.uni_augsburg.bazi.common.PluginManager;
import de.uni_augsburg.bazi.common.data.Data;
import de.uni_augsburg.bazi.common.data.MapData;

import java.util.Map;

class PluginConverter<T extends Plugin.Instance> implements ObjectConverter<T>
{
	private final Class<T> type;

	public PluginConverter(Class<T> type)
	{
		this.type = type;
	}

	@Override public Object serialize(T value)
	{
		return value;
	}

	@Override public T deserialize(Object value)
	{
		Plugin.Params params;
		if (value instanceof Data) params = ((Data) value).cast(Plugin.Params.class);
		else if (value instanceof Map<?, ?>) params = new MapData((Map<?, ?>) value).cast(Plugin.Params.class);
		else params = value::toString;
		return PluginManager.tryInstantiate(type, params).get();
	}
}