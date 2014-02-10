package de.uni_augsburg.bazi.common;

import java.util.*;
import java.util.function.Function;

public class MList<V> extends AbstractList<V> implements List<V>
{
	private final List<V> list = new ArrayList<>();
	private final Map<Object, V> map = new HashMap<>();
	private final Function<V, ? extends Object> keyFunction;

	public MList()
	{
		this.keyFunction = Function.identity();
	}

	public MList(Function<V, ? extends Object> keyFunction)
	{
		this.keyFunction = keyFunction;
	}

	public MList(Collection<? extends V> c)
	{
		this.keyFunction = Function.identity();
		addAll(c);
	}

	public MList(Function<V, Object> keyFunction, Collection<? extends V> c)
	{
		this.keyFunction = keyFunction;
		addAll(c);
	}


	@Override
	public V get(int index)
	{
		return list.get(index);
	}

	@Override
	public int size()
	{
		return list.size();
	}

	@Override
	public V set(int index, V element)
	{
		V old = list.set(index, element);
		map.remove(keyFunction.apply(old));
		map.put(keyFunction.apply(element), element);
		return old;
	}

	@Override
	public void add(int index, V element)
	{
		list.add(index, element);
		map.put(keyFunction.apply(element), element);
	}

	@Override
	public V remove(int index)
	{
		V old = list.remove(index);
		map.remove(keyFunction.apply(old));
		return old;
	}

	public V find(Object key)
	{
		return map.get(key);
	}
}
