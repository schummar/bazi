package de.uni_augsburg.bazi.common.util;

import de.uni_augsburg.bazi.common.Data;
import de.uni_augsburg.bazi.common.format.Converter;
import de.uni_augsburg.bazi.common.format.ListConverter;
import de.uni_augsburg.bazi.common.format.ObjectConverter;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Converter(MList.Converter.class)
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

	@Override public String toString()
	{
		if (stream().anyMatch(
			v ->
				v instanceof Data || v instanceof List || v instanceof Map
		))
			return "[\n  " + stream().map(Object::toString).collect(Collectors.joining(",\n")).replaceAll("\n", "\n  ") + "\n]";
		return super.toString();
	}

	public static <V> Collector<V, MList<V>, MList<V>> collector()
	{
		return Collector.of(
			MList<V>::new,
			MList<V>::add,
			(a, b) -> {
				a.addAll(b);
				return a;
			}
		);
	}

	@SafeVarargs public static <V> MList<V> of(V... vs)
	{
		return new MList<>(Arrays.asList(vs));
	}

	public static class Converter implements ListConverter<MList<?>>
	{
		@Override public List<?> serialize(MList<?> value) { return value; }
		@Override public MList<?> deserialize(List<?> list) { return new MList<>(list); }
	}
}
