package de.uni_augsburg.bazi.common.util;

import de.uni_augsburg.bazi.common.Resources;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
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

	public static <A, B> void forEachPair(Iterable<A> ia, Iterable<B> ib, BiConsumer<A, B> consumer)
	{
		Iterator<A> a = ia.iterator();
		Iterator<B> b = ib.iterator();
		while (a.hasNext() && b.hasNext())
			consumer.accept(a.next(), b.next());
		if (a.hasNext() || b.hasNext())
			throw new RuntimeException(Resources.get("error.different_length"));
	}

	public static <A, B> void forEachCombination(Iterable<A> ia, Iterable<B> ib, BiConsumer<A, B> consumer)
	{
		for (A a : ia)
			for (B b : ib)
				consumer.accept(a, b);
	}

	public static <T, A, B> List<List<T>> createTable(Iterable<A> ia, Iterable<B> ib, BiFunction<A, B, T> function)
	{
		List<List<T>> table = new ArrayList<>();
		for (A a : ia)
		{
			List<T> row = new ArrayList<>();
			for (B b : ib)
				row.add(function.apply(a, b));
			table.add(row);
		}
		return table;
	}
}
