package de.uni_augsburg.bazi.common;

import de.uni_augsburg.bazi.common.data.Data;

import java.util.List;
import java.util.Optional;

/**
 * Created by Marco on 26.02.14.
 */
public interface Plugin<T extends Plugin.Instance>
{
	Class<? extends T> getInstanceType();
	List<Object> getParamAttributes();
	Optional<? extends T> tryInstantiate(Params params);

	public interface Params extends Data
	{
		String name();
	}

	public interface Instance{}
}
