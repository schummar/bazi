package de.uni_augsburg.bazi.dir;

import de.uni_augsburg.bazi.common.Plugin;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/** This plugin produces instances of DirFilter. */
public class DirFilterPlugin implements Plugin<DirFilter>
{
	@Override public Class<? extends DirFilter> getInstanceType()
	{
		return DirFilter.class;
	}
	@Override public List<Object> getParamAttributes()
	{
		return Collections.emptyList();
	}
	@Override public Optional<? extends DirFilter> tryInstantiate(Params params)
	{
		return Optional.of(new DirFilter());
	}
}
