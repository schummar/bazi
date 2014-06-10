package de.schummar.castable;

import javafx.beans.InvalidationListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;

import java.util.AbstractMap;
import java.util.Set;

public class CMap<T> extends AbstractMap<String,T> implements ObservableMap<String,T>
{
	@Override public Set<Entry<String, T>> entrySet()
	{
		return null;
	}
	@Override public void addListener(MapChangeListener<? super String, ? super T> listener)
	{

	}
	@Override public void removeListener(MapChangeListener<? super String, ? super T> listener)
	{

	}
	@Override public void addListener(InvalidationListener listener)
	{

	}
	@Override public void removeListener(InvalidationListener listener)
	{

	}
}
