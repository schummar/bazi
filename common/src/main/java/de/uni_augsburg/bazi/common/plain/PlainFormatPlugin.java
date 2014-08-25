package de.uni_augsburg.bazi.common.plain;

import de.uni_augsburg.bazi.common.Plugin;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/** Plugin that creates PlainFormat instances. */
public class PlainFormatPlugin implements Plugin<PlainFormat>
{
	@Override public Class<? extends PlainFormat> getInstanceType()
	{
		return PlainFormat.class;
	}
	@Override public List<Object> getParamAttributes()
	{
		return Collections.emptyList();
	}
	@Override public Optional<? extends PlainFormat> tryInstantiate(Params params)
	{
		if (!params.name().equals("plain")) return Optional.empty();
		return Optional.of(new PlainFormat(params.cast(PlainOptions.class)));
	}
}
