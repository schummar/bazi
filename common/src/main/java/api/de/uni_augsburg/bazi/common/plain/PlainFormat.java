package de.uni_augsburg.bazi.common.plain;

import de.schummar.castable.Castable;
import de.schummar.castable.Data;
import de.uni_augsburg.bazi.common.format.Format;

import javax.naming.OperationNotSupportedException;
import java.io.InputStream;
import java.io.OutputStream;

/** The PlainFormat is a human-readable output format */
public class PlainFormat implements Format
{
	private PlainOptions options;

	/**
	 * Constructor with options initializer.
	 * @param options the options.
	 */
	public PlainFormat(PlainOptions options)
	{
		this.options = options;
	}

	@Override public void configure(Data data)
	{
		options.merge(data);
	}


	@Override public Castable deserialize(InputStream stream)
	{
		throw new RuntimeException(new OperationNotSupportedException());
	}
	@Override public void serialize(Castable data, OutputStream stream)
	{
		/*if (data.plain() != null)
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
		return null;*/
	}
}
