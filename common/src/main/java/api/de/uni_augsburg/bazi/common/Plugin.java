package de.uni_augsburg.bazi.common;

import java.util.List;
import java.util.Optional;

/**
 * Created by Marco on 26.02.14.
 */
public interface Plugin<T>
{
	Class<? extends T> getInstanceType();
	List<Object> getParamAttributes();
	Optional<? extends T> tryInstantiate(Params params);

	public interface Params extends Data
	{
		String name();
	}
}