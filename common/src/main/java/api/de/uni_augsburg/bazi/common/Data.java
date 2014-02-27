package de.uni_augsburg.bazi.common;

import de.uni_augsburg.bazi.common.format.Converter;
import de.uni_augsburg.bazi.common.format.ObjectConverter;

import java.util.Map;

@Converter(Data.Converter.class)
public interface Data
{
	default boolean isMutable() {return true;}

	default <T extends Data> T cast(Class<T> type)
	{
		if (type.isInstance(this))
			return type.cast(this);

		return MapData.fromDataInterface(this).cast(type);
	}

	default Data copy()
	{
		return MapData.fromDataInterface(this);
	}

	default Data immutable()
	{
		return MapData.fromDataInterface(this).immutable();
	}

	default Map<String, Object> serialize() { return MapData.fromDataInterface(this).serialize(); }


	public static class Converter implements ObjectConverter<Data>
	{
		@Override public Object serialize(Data value)
		{
			return value.serialize();
		}
		@Override public Data deserialize(Object value)
		{
			if (value instanceof Map<?, ?>) return new MapData((Map<?, ?>) value);
			return new MapData();
		}
	}
}
