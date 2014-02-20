package de.uni_augsburg.bazi.common;

public interface Data
{
	public default boolean isMutable() {return true;}

	public default <T extends Data> T cast(Class<T> type)
	{
		if (type.isInstance(this))
			return type.cast(this);

		return MapData.fromDataInterface(this).cast(type);
	}

	public default Data copy()
	{
		return MapData.fromDataInterface(this);
	}

	public default Data immutable()
	{
		return MapData.fromDataInterface(this).immutable();
	}

	public default MapData asMap()
	{
		return MapData.fromDataInterface(this);
	}
}
