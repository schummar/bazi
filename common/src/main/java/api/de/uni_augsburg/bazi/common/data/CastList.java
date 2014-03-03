package de.uni_augsburg.bazi.common.data;

import java.lang.reflect.Type;
import java.util.AbstractList;
import java.util.List;

import static de.uni_augsburg.bazi.common.data.CastHelper.cast;

/**
 * Created by Marco on 03.03.14.
 */
@SuppressWarnings("unchecked")
public class CastList<T> extends AbstractList<T>
{
	private final List delegate;
	private final Type type;
	public CastList(List delegate, Type type)
	{
		while (delegate instanceof CastList) delegate = ((CastList) delegate).delegate;
		this.delegate = delegate;
		this.type = type;
	}
	@Override public T get(int index)
	{
		T t = cast(delegate.get(index), type);
		set(index, t);
		return t;
	}
	@Override public int size()
	{
		return delegate.size();
	}
	@Override public void add(int index, T t)
	{
		delegate.add(index, t);
	}
	@Override public T set(int index, T element)
	{
		return cast(delegate.set(index, element), type);
	}
	@Override public T remove(int index)
	{
		return cast(delegate.remove(index), type);
	}
}
