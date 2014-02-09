package de.uni_augsburg.bazi.cl;

import de.uni_augsburg.bazi.common.Json;

public enum Format
{
	PLAIN
		{
			@Override
			public String serialize(Formatter formatter)
			{
				return formatter.plain();
			}
			@Override
			public <T> T deserialize(String s, Class<T> c)
			{
				throw new UnsupportedOperationException();
			}
		},

	JSON
		{
			@Override
			public String serialize(Formatter formatter)
			{
				return formatter.json();
			}
			@Override
			public <T> T deserialize(String s, Class<T> c)
			{
				return Json.fromJson(s, c);
			}
		};

	public abstract String serialize(Formatter formatter);
	public abstract <T> T deserialize(String s, Class<T> c);

	public interface Formatter
	{
		public String plain();
		public String json();
	}
}
