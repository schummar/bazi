package de.uni_augsburg.bazi.common;

import de.uni_augsburg.bazi.common.format.Converter;
import de.uni_augsburg.bazi.common.format.ObjectConverter;

import java.util.Map;

@Converter(Data.Converter.class)
public interface Data
{
	default <T extends Data> T cast(Class<? extends T> type) { return MapData.fromDataInterface(this).cast(type); }
	default Data merge(Data value) { return MapData.fromDataInterface(this).merge(value); }
	default Data copy()
	{
		return MapData.fromDataInterface(this);
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
			if (value instanceof Data) return (Data) value;
			if (value instanceof Map<?, ?>) return new MapData((Map<?, ?>) value);
			return new MapData();
		}
	}
}
