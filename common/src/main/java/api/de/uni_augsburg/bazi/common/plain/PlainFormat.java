package de.uni_augsburg.bazi.common.plain;

import de.uni_augsburg.bazi.common.StringTable;
import de.uni_augsburg.bazi.common.data.Data;
import de.uni_augsburg.bazi.common.format.Format;

import javax.naming.OperationNotSupportedException;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Marco on 06.03.14.
 */
public class PlainFormat implements Format
{
	private PlainOptions options;
	public PlainFormat(Data data)
	{
		options = data.cast(PlainOptions.class);
		if (options.divisorFormat() == null) options.divisorFormat(DivisorFormat.DIV_SPLIT);
		if (options.orientation() == null) options.oritentation(Orientation.VERTICAL);
		if (options.tieFormat() == null) options.tieFormat(TieFormat.CODED);
		if (options.maxDigits() == null) options.maxDigits(16);
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
			return data.plain().get(options).stream()
				.map(StringTable::toString)
				.collect(Collectors.joining("\n\n\n"));

		return null;
	}
}