package de.uni_augsburg.bazi.common.util;

import de.uni_augsburg.bazi.common.Resources;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

/** Utility class for Collections. */
public class CollectionHelper
{
	private CollectionHelper() {}


	/**
	 * return the first element that tests positive for the given predicate.
	 * @param <T> the type of the element.
	 * @param collection the collection in which to look.
	 * @param predicate the predicate to test.
	 * @return the first element that tests positive for the predicate or null if none exists.
	 */
	public static <T> T find(Collection<? extends T> collection, Predicate<T> predicate)
	{
		for (T t : collection)
			if (predicate.test(t))
				return t;
		return null;
	}


	/**
	 * Simultaneously iterates over two Iterables and calls the consumer for each value pair.
	 * @param <A> the content type of the first Iterable.
	 * @param <B> the content type of the second Iterable.
	 * @param ia the first Iterable.
	 * @param ib the second Iterable.
	 * @param consumer the consumer to call for each value pair.
	 */
	public static <A, B> void forEachPair(Iterable<A> ia, Iterable<B> ib, BiConsumer<A, B> consumer)
	{
		Iterator<A> a = ia.iterator();
		Iterator<B> b = ib.iterator();
		while (a.hasNext() && b.hasNext())
			consumer.accept(a.next(), b.next());
		if (a.hasNext() || b.hasNext())
			throw new RuntimeException(Resources.get("error.different_length"));
	}


	/**
	 * Iterates over the cartesian product of two Iterables and calls the consumer for each value pair.
	 * @param <A> the content type of the first Iterable.
	 * @param <B> the content type of the second Iterable.
	 * @param ia the first Iterable.
	 * @param ib the second Iterable.
	 * @param consumer the consumer to call for each value pair.
	 */
	public static <A, B> void forEachCombination(Iterable<A> ia, Iterable<B> ib, BiConsumer<A, B> consumer)
	{
		for (A a : ia)
			for (B b : ib)
				consumer.accept(a, b);
	}
}
