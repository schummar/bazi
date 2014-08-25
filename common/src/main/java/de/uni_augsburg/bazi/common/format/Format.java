package de.uni_augsburg.bazi.common.format;

import de.schummar.castable.Castable;
import de.schummar.castable.Data;
import de.uni_augsburg.bazi.common.Plugin;

import java.io.*;

/** A format plugin is used to convert file formats (json,xml,...) to raw data and vice versa. */
public interface Format extends Plugin.Instance
{
	/**
	 * Deserialize the given String to a castable.
	 * @param reader is used to read the serialized data from.
	 * @return a castable.
	 */
	Castable deserialize(Reader reader);
	default Castable deserialize(File file)
	{
		try (Reader reader = new InputStreamReader(new FileInputStream(file)))
		{
			return deserialize(reader);
		}
		catch (IOException ignore) {}
		return null;
	}

	/**
	 * Serialize the given castable.
	 * @param data the castable.
	 * @param writer is used to write the serialized data to.
	 */
	void serialize(Castable data, PrintStream writer);
	default String serialize(Castable data)
	{
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		PrintStream writer = new PrintStream(byteArrayOutputStream);
		serialize(data, writer);
		writer.flush();
		writer.close();
		return byteArrayOutputStream.toString();
	}

	/**
	 * Configure the format.
	 * Usually plugins are configured on creation. But since input files may contain format
	 * parameters the configuration can be updated.
	 * @param data the configuration data.
	 */
	void configure(Data data);
}
