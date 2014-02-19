package de.uni_augsburg.bazi.common;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;

public class MList<V> extends ArrayList<V>
{
	public MList(int initialCapacity)
	{
		super(initialCapacity);
	}
	public MList()
	{
		super();
	}
	public MList(Collection<? extends V> c)
	{
		super(c);
	}

	public V find(Predicate<V> predicate)
	{
		return stream().filter(predicate).findAny().orElse(null);
	}

	public MList<V> findAll(Predicate<V> predicate)
	{
		return stream().filter(predicate).collect(collector());
	}

	public static <V> Collector<V, MList<V>, MList<V>> collector()
	{
		return Collector.of(
			MList::new,
			MList::add,
			(a, b) -> {
				a.addAll(b);
				return a;
			}
		);
	}

	public static <V> MList<V> of(V... vs)
	{
		return new MList<>(Arrays.asList(vs));
	}
}
