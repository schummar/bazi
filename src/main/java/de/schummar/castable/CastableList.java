package de.schummar.castable;

import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

public class CastableList extends SimpleListProperty<Castable> implements Castable
{
	public CastableList()
	{
		super(FXCollections.observableArrayList());
		addListener(changeListener);
	}


	private final List<InvalidationListener> deepListeners = new ArrayList<>();
	@Override public void addDeepListener(InvalidationListener invalidationListener)
	{
		deepListeners.add(invalidationListener);
	}
	@Override public void removeDeepListener(InvalidationListener invalidationListener)
	{
		deepListeners.remove(invalidationListener);
	}
	private InvalidationListener deepListener = observable -> {
		deepListeners.forEach(l -> l.invalidated(this));
	};
	private ListChangeListener<Castable> changeListener = change -> {
		while (change.next())
		{
			change.getRemoved().forEach(v -> v.removeDeepListener(deepListener));
			change.getAddedSubList().forEach(v -> v.addDeepListener(deepListener));
		}
	};


	@Override public Castable copy()
	{
		CastableList copy = new CastableList();
		forEach(v -> copy.add(v.copy()));
		return copy;
	}
	@Override public void overwrite(Castable castable)
	{
		CastableList that = castable.asCastableList();
		clear();
		addAll(that);
	}
	@Override public CastableList asCastableList() { return this; }

	@Override public String toString()
	{
		return getValue().toString();
	}


	public <T> List<T> cast(Converter<T> adapter) { return new View<>(adapter); }
	private class View<T> extends AbstractList<T>
	{
		private final Converter<T> adapter;
		private View(Converter<T> adapter)
		{
			this.adapter = adapter;
		}
		@Override public T get(int index)
		{
			return adapter.apply(CastableList.this.get(index));
		}
		@Override public int size()
		{
			return CastableList.this.size();
		}
		@Override public T set(int index, T element)
		{
			return adapter.apply(CastableList.this.set(index, adapter.applyInverse(element)));
		}
		@Override public void add(int index, T element)
		{
			CastableList.this.add(index, adapter.applyInverse(element));
		}
		@Override public T remove(int index)
		{
			return adapter.apply(CastableList.this.remove(index));
		}
	}
}
