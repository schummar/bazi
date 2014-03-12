package de.uni_augsburg.bazi.common.data;

import de.uni_augsburg.bazi.common.format.Converter;
import de.uni_augsburg.bazi.common.format.ObjectConverter;
import de.uni_augsburg.bazi.common.plain.PlainSupplier;

import java.util.Map;

@Converter(Data.Converter.class)
public interface Data
{
	static <T extends Data> T create(Class<T> type) { return new MapData().cast(type); }


	default <T extends Data> T cast(Class<? extends T> type) { return MapData.fromDataInterface(this).cast(type); }
	default Data merge(Data value) { return MapData.fromDataInterface(this).merge(value); }
	default Data copy() { return MapData.fromDataInterface(this); }
	default <T extends Data> T copy(Class<T> type) { return MapData.fromDataInterface(this).copy(type); }
	default <T extends Data> T crop(Class<T> type) { return MapData.fromDataInterface(this).crop(type); }
	default MapData toMapData() { return MapData.fromDataInterface(this).toMapData(); }
	default PlainSupplier plain() { return MapData.fromDataInterface(this).plain(); }
	default Data plain(PlainSupplier plain) { return MapData.fromDataInterface(this).plain(plain); }

	public static class Converter implements ObjectConverter<Data>
	{
		@Override public Object serialize(Data value)
		{
			return value.toMapData();
		}
		@Override public Data deserialize(Object value)
		{
			if (value instanceof Data) return (Data) value;
			if (value instanceof Map<?, ?>) return new MapData((Map<?, ?>) value);
			return new MapData();
		}
	}
}
