package de.uni_augsburg.bazi.common.data;

import java.lang.reflect.Type;
import java.util.*;

import static de.uni_augsburg.bazi.common.data.CastHelper.cast;

/**
 * Created by Marco on 03.03.14.
 */
@SuppressWarnings("unchecked")
public class CastMap<V> extends AbstractMap<Object, V>
{
	private final Set<Entry<Object, V>> delegateSet;

	public CastMap(Map delegate, Type valueType)
	{
		this.delegateSet = new AbstractSet<Entry<Object, V>>()
		{
			@Override public Iterator<Entry<Object, V>> iterator()
			{
				return new Iterator<Entry<Object, V>>()
				{
					private final Iterator<Entry<Object, Object>> delegateIterator = delegate.entrySet().iterator();
					@Override public boolean hasNext()
					{
						return delegateIterator.hasNext();
					}
					@Override public Entry<Object, V> next()
					{
						return new Entry<Object, V>()
						{
							private final Entry delegateEntry = delegateIterator.next();
							@Override public Object getKey()
							{
								return delegateEntry.getKey();
							}
							@Override public V getValue()
							{
								V v = cast(delegateEntry.getValue(), valueType);
								delegateEntry.setValue(v);
								return v;
							}
							@Override public V setValue(V value)
							{
								return cast(delegateEntry.setValue(value), valueType);
							}
						};
					}
					@Override public void remove()
					{
						delegateIterator.remove();
					}
				};
			}
			@Override public int size()
			{
				return delegate.size();
			}
			@Override public boolean add(Entry<Object, V> kvEntry)
			{
				return delegate.put(kvEntry.getKey(), kvEntry.getValue()) != kvEntry.getValue();
			}
		};
	}


	@Override public Set<Entry<Object, V>> entrySet()
	{
		return delegateSet;
	}
}
