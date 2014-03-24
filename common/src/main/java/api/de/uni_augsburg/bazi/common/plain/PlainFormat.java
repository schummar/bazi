package de.uni_augsburg.bazi.common.plain;

import de.uni_augsburg.bazi.common.StringTable;
import de.uni_augsburg.bazi.common.Version;
import de.uni_augsburg.bazi.common.data.Data;
import de.uni_augsburg.bazi.common.format.Format;

import javax.naming.OperationNotSupportedException;
import java.util.Map;
import java.util.stream.Collectors;

/** The PlainFormat is a human-readable output format */
public class PlainFormat implements Format
{
	private PlainOptions options;

	/**
	 * Constructor with options initializer.
	 * @param data the options.
	 */
	public PlainFormat(Data data)
	{
		options = data.cast(PlainOptions.class);
	}

	@Override public void configure(Data data)
	{
		options.merge(data);
	}


	@Override public Map<String, Object> deserialize(String s)
	{
		throw new RuntimeException(new OperationNotSupportedException());
	}
	@Override public String serialize(Data data)
	{
		if (data.plain() != null)
		{
			String s = "************************************************************\n";
			s += data.plain().get(options).stream()
				.map(StringTable::toString)
				.collect(Collectors.joining("\n\n"));
			if (!s.endsWith("\n")) s += "\n";
			s += "BAZI " + Version.getCurrentVersionName() + " - Made in Augsburg University\n";
			s += "************************************************************\n";
			return s;
		}
		return null;
	}
}
