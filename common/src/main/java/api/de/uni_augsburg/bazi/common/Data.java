package de.uni_augsburg.bazi.common;

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

	default MapData asMap()
	{
		return MapData.fromDataInterface(this);
	}
}
