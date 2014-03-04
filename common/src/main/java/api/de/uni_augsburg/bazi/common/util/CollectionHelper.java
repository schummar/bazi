package de.uni_augsburg.bazi.common.util;

import java.util.Collection;
import java.util.function.Predicate;

/**
 * Created by Marco on 04.03.14.
 */
public class CollectionHelper
{
	public static <T> T find(Collection<? extends T> collection, Predicate<T> predicate)
	{
		for (T t : collection)
			if (predicate.test(t))
				return t;
		return null;
	}
}
