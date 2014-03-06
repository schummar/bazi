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
	@Override public Map<String, Object> deserialize(String s)
	{
		throw new RuntimeException(new OperationNotSupportedException());
	}
	@Override public String serialize(Data data)
	{
		if (data.plain() != null)
			return data.plain().get(
				new PlainOptions(PlainOptions.Orientation.VERTICAL, PlainOptions.DivisorFormat.QUOTIENT, PlainOptions.TieFormat.CODED)
			).stream()
				.map(StringTable::toString)
				.collect(Collectors.joining("\n\n\n"));

		return null;
	}
}
