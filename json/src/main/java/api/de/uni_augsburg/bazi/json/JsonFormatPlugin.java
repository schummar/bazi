package de.uni_augsburg.bazi.json;

import de.uni_augsburg.bazi.common.Plugin;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class JsonFormatPlugin implements Plugin<JsonFormat>
{
	@Override public Class<? extends JsonFormat> getInstanceType() { return JsonFormat.class; }
	@Override public List<Object> getParamAttributes() { return Collections.emptyList(); }

	@Override public Optional<? extends JsonFormat> tryInstantiate(Params params)
	{
		return params.name().equals("json")
			? Optional.of(new JsonFormat())
			: Optional.empty();
	}
}
