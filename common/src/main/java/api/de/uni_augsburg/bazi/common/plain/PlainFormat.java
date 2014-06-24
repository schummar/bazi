package de.uni_augsburg.bazi.common.plain;

import de.schummar.castable.Castable;
import de.schummar.castable.Data;
import de.uni_augsburg.bazi.common.StringTable;
import de.uni_augsburg.bazi.common.Version;
import de.uni_augsburg.bazi.common.format.Format;

import javax.naming.OperationNotSupportedException;
import java.io.PrintStream;
import java.io.Reader;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/** The PlainFormat is a human-readable output format */
public class PlainFormat implements Format
{
	private PlainOptions options;
	private BiFunction<Data, PlainOptions, List<StringTable>> formatter = null;

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

	public void configure(BiFunction<Data, PlainOptions, List<StringTable>> formatter)
	{
		this.formatter = formatter;
	}


	@Override public Castable deserialize(Reader reader)
	{
		throw new RuntimeException(new OperationNotSupportedException());
	}
	@Override public void serialize(Castable data, PrintStream writer)
	{
		if (formatter == null) return;

		String output = formatter.apply(data.asCastableObject(), options).stream()
			.map(StringTable::toString)
			.collect(Collectors.joining("\n\n"));
		if (!output.endsWith("\n")) output += "\n";

		try
		{
			writer.println("************************************************************");
			writer.print(output);
			writer.print("BAZI ");
			writer.print(Version.getCurrentVersionName());
			writer.println(" - Made in Augsburg University");
			writer.println("************************************************************");
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
}
