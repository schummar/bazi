package de.uni_augsburg.bazi.common.data;

import java.lang.reflect.Type;
import java.util.*;

import static de.uni_augsburg.bazi.common.data.CastHelper.cast;

@SuppressWarnings("unchecked")
public class CastMap<V> implements Map<Object, V>
{
	private final Map delegate;
	private final Type type;

	public CastMap(Map delegate, Type type)
	{
		this.type = type;
		while (delegate instanceof CastMap) delegate = ((CastMap) delegate).delegate;
		this.delegate = delegate;
	}


	@Override public int size()
	{
		return delegate.size();
	}
	@Override public boolean isEmpty()
	{
		return delegate.isEmpty();
	}
	@Override public boolean containsKey(Object key)
	{
		return delegate.containsKey(key);
	}
	@Override public boolean containsValue(Object value)
	{
		return delegate.containsValue(value);
	}
	@Override public V get(Object key)
	{
		if (!delegate.containsKey(key)) return null;
		V v = cast(delegate.get(key), type);
		delegate.put(key, v);
		return v;
	}
	@Override public V put(Object key, V value)
	{
		return cast(delegate.put(key, value), type);
	}
	@Override public V remove(Object key)
	{
		return cast(delegate.remove(key), type);
	}
	@Override public void putAll(Map<?, ? extends V> m)
	{
		delegate.putAll(m);
	}
	@Override public void clear()
	{
		delegate.clear();
	}
	@Override public Set<Object> keySet()
	{
		return delegate.keySet();
	}
	@Override public Collection<V> values()
	{
		replaceAll((k, v) -> cast(v, type));
		return delegate.values();
	}
	@Override public Set<Entry<Object, V>> entrySet()
	{
		return new AbstractSet<Entry<Object, V>>()
		{
			@Override public Iterator<Entry<Object, V>> iterator()
			{
				return new Iterator<Entry<Object, V>>()
				{
					private final Iterator<Entry> iterator = delegate.entrySet().iterator();
					@Override public boolean hasNext()
					{
						return iterator.hasNext();
					}
					@Override public Entry<Object, V> next()
					{
						Entry entry = iterator.next();
						return new Entry<Object, V>()
						{
							@Override public Object getKey()
							{
								return entry.getKey();
							}
							@Override public V getValue()
							{
								V v = cast(entry.getValue(), type);
								entry.setValue(v);
								return v;
							}
							@Override public V setValue(V value)
							{
								return cast(entry.setValue(value), type);
							}
						};
					}
					@Override public void remove()
					{
						iterator.remove();
					}
				};
			}
			@Override public int size()
			{
				return delegate.entrySet().size();
			}
			@Override public boolean add(Entry<Object, V> objectVEntry)
			{
				return delegate.entrySet().add(objectVEntry);
			}
		};
	}
}
