package de.schummar.castable;

import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;

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
	private InvalidationListener deepListener = observable -> deepListeners.forEach(l -> l.invalidated(this));
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
	@Override public void merge(Castable castable)
	{
		CastableList that = castable.asCastableList();
		for (int i = 0; i < size() && i < that.size(); i++)
			get(i).merge(that.get(i));
		for (int i = size(); i < that.size(); i++)
			add(that.get(i));
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
}
