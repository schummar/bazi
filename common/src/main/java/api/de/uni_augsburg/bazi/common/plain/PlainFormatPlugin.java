package de.uni_augsburg.bazi.common.plain;

import de.uni_augsburg.bazi.common.Plugin;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by Marco on 06.03.14.
 */
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
		return Optional.of(new PlainFormat());
	}
}
