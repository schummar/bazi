package de.schummar.castable;

import javafx.beans.InvalidationListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;

import java.util.*;

public class CMap<T> implements ObservableMap<String, T>
{
	protected final CastableObject map;
	protected final Converter<T> converter;
	protected final List<InvalidationListener> invalidationListeners = new ArrayList<>();
	protected final List<MapChangeListener<? super String, ? super T>> mapChangeListeners = new ArrayList<>();
	public CMap(CastableObject map, Converter<T> converter)
	{
		this.map = map;
		this.converter = converter;
		map.addListener(invalidationListener);
		map.addListener(mapChangeListener);
	}


	private InvalidationListener invalidationListener = observable -> invalidationListeners.forEach(listener -> listener.invalidated(this));
	private MapChangeListener<? super String, ? super Castable> mapChangeListener = change -> {
		MapChangeListener.Change<String, T> tChange = new MapChangeListener.Change<String, T>(this)
		{
			@Override public boolean wasAdded() { return change.wasAdded(); }
			@Override public boolean wasRemoved() { return change.wasRemoved(); }
			@Override public String getKey() { return change.getKey(); }
			@Override public T getValueAdded() { return converter.unpack(change.getValueAdded()); }
			@Override public T getValueRemoved() { return converter.unpack(change.getValueRemoved()); }
		};
		mapChangeListeners.forEach(listener -> listener.onChanged(tChange));
	};


	@Override public void addListener(MapChangeListener<? super String, ? super T> listener)
	{
		mapChangeListeners.add(listener);
	}
	@Override public void removeListener(MapChangeListener<? super String, ? super T> listener)
	{
		mapChangeListeners.remove(listener);
	}
	@Override public int size()
	{
		return map.size();
	}
	@Override public boolean isEmpty()
	{
		return map.isEmpty();
	}
	@Override public boolean containsKey(Object key)
	{
		return map.containsKey(key);
	}
	@Override public boolean containsValue(Object value)
	{
		for (T t : values())
			if (t.equals(value)) return true;
		return false;
	}
	@Override public T get(Object key)
	{
		if (!(key instanceof String)) return null;
		Castable v = map.get(key);
		if (v == null) map.put((String) key, v = converter.pack(null));
		return converter.unpack(v);
	}
	@Override public T put(String key, T value)
	{
		T old = get(key);
		map.put(key, converter.pack(value));
		return old;
	}
	@Override public T remove(Object key)
	{
		T old = get(key);
		map.remove(key);
		return old;
	}
	@Override public void putAll(Map<? extends String, ? extends T> m)
	{
		m.forEach(this::put);
	}
	@Override public void clear()
	{
		map.clear();
	}
	@Override public Set<String> keySet()
	{
		return map.keySet();
	}
	@Override public Collection<T> values()
	{
		return values;
	}
	@Override public Set<Entry<String, T>> entrySet()
	{
		return entrySet;
	}
	@Override public void addListener(InvalidationListener listener)
	{
		invalidationListeners.add(listener);
	}
	@Override public void removeListener(InvalidationListener listener)
	{
		invalidationListeners.remove(listener);
	}


	private final Collection<T> values = new AbstractCollection<T>()
	{
		@Override public Iterator<T> iterator()
		{
			Iterator<Castable> iterator = map.values().iterator();
			return new Iterator<T>()
			{
				@Override public boolean hasNext()
				{
					return iterator.hasNext();
				}
				@Override public T next()
				{
					return converter.unpack(iterator.next());
				}
			};
		}
		@Override public int size()
		{
			return map.size();
		}
	};


	private final Set<Entry<String, T>> entrySet = new AbstractSet<Entry<String, T>>()
	{
		@Override public Iterator<Entry<String, T>> iterator()
		{
			Iterator<Entry<String, Castable>> iterator = map.entrySet().iterator();
			return new Iterator<Entry<String, T>>()
			{
				@Override public boolean hasNext() { return iterator.hasNext(); }
				@Override public Entry<String, T> next()
				{
					Entry<String, Castable> entry = iterator.next();
					return new Entry<String, T>()
					{
						@Override public String getKey() { return entry.getKey(); }
						@Override public T getValue() { return converter.unpack(entry.getValue()); }
						@Override public T setValue(T value) { return converter.unpack(entry.setValue(converter.pack(value))); }
					};
				}
			};
		}
		@Override public int size()
		{
			return map.size();
		}
	};
}
