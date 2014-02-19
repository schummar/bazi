package de.uni_augsburg.bazi.common;

public interface Data
{
	public default <T extends Data> T cast(Class<T> type)
	{
		System.out.println("Data::cast");
		if (type.isInstance(this))
			return type.cast(this);

		return new GenericData(this).cast(type);
	}

	public default <T> T copy(Class<T> type)
	{
		System.out.println("Data::copy");
		return null;
	}
}
