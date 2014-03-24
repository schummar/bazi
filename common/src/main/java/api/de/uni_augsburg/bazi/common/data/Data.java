package de.uni_augsburg.bazi.common.data;

import de.uni_augsburg.bazi.common.format.Converter;
import de.uni_augsburg.bazi.common.format.ObjectConverter;
import de.uni_augsburg.bazi.common.plain.PlainSupplier;

import java.util.Map;

/**
 * Generic Data interface.
 * All interfaces that extend {@link Data} are assumed to follow these conventions:
 * <ul>
 * <li>A non-void method without any parameters is a getter for the attribute with the method's name.</li>
 * <li>A void method with exactly one parameter is a setter for the attribute withe the method's name.</li>
 * <li>There are no other methos. Except, of course, the ones defined in {@link Data}.</li>
 * </ul>
 * <p>
 * The advantage of those restrictions is, that such a data object can be dynamically deep-cast to any other Data class.
 */
@Converter(Data.Converter.class)
public interface Data
{
	/**
	 * Creates an empty Data object of type <b>type</b>.
	 * @param <T> the desired type. Must be a subtype of {@link Data}.
	 * @param type the class of the desired type.
	 * @return an empty object with type <b>type</b>.
	 */
	static <T extends Data> T create(Class<T> type)
	{
		return new MapData().cast(type);
	}


	/**
	 * Cast to another Data type.
	 * @param <T> the desired type. Must be a subtype of {@link Data}.
	 * @param type the class of the desired type.
	 * @return a view of this, with type <b>type</b>.
	 */
	default <T extends Data> T cast(Class<? extends T> type)
	{
		return MapData.fromDataInterface(this).cast(type);
	}


	/**
	 * Merge another data object into this one.
	 * Attributes with the same name are merged if possible, the one in this overwritten otherwise.
	 * @param value the object to be merged into this.
	 * @return a view of this, with merged <b>value</b>.
	 */
	default Data merge(Data value)
	{
		return MapData.fromDataInterface(this).merge(value);
	}


	/**
	 * Copy this object.
	 * @return a copy of this.
	 */
	default Data copy()
	{
		return MapData.fromDataInterface(this);
	}


	/**
	 * Copy only the part of this object that is required by the target type <b>type</b>.
	 * @param <T> the desired type. Must be a subtype of {@link Data}.
	 * @param type the class of the desired type.
	 * @return a copy of only the part of this object that is required by the target type <b>type</b>.
	 */
	default <T extends Data> T copy(Class<T> type)
	{
		return MapData.fromDataInterface(this).copy(type);
	}


	/**
	 * Crop anything of this object that is not required by the target type <b>type</b>.
	 * @param <T> the desired type. Must be a subtype of {@link Data}.
	 * @param type the class of the desired type.
	 * @return a view of this object, cropped to the target type <b>type</b>.
	 */
	default <T extends Data> T crop(Class<T> type)
	{
		return MapData.fromDataInterface(this).crop(type);
	}


	/**
	 * A {@link de.uni_augsburg.bazi.common.data.MapData} representation of this, which is both a Data object and a map.
	 * @return a {@link de.uni_augsburg.bazi.common.data.MapData} representation of this.
	 */
	default MapData toMapData()
	{
		return MapData.fromDataInterface(this).toMapData();
	}


	/**
	 * Converts to a map of raw data.
	 * Each entry represents an attribute.
	 * The key is the attribute name.
	 * The value must either be a String, a List of values or again an attribute map.
	 * @return a raw data map.
	 */
	default Map<String, Object> toRawData()
	{
		return MapData.fromDataInterface(this).toRawData();
	}


	/**
	 * A supplier that generates a plain, human-readable representation of this data object.
	 * @return a supplier that generates a plain,	human-readable representation of this data object.
	 */
	default PlainSupplier plain()
	{
		return MapData.fromDataInterface(this).plain();
	}


	/**
	 * A supplier that generates a plain, human-readable representation of this data object.
	 * @param plain a supplier that generates a plain,	human-readable representation of this data object.
	 * @return this.
	 */
	default Data plain(PlainSupplier plain)
	{
		return MapData.fromDataInterface(this).plain(plain);
	}


	/** @see de.uni_augsburg.bazi.common.format.ObjectConverter */
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
