package de.schummar.castable;

import javafx.beans.InvalidationListener;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;

import java.util.Collection;
import java.util.Iterator;

public class CSet<T> implements ObservableSet<T>
{
	@Override public void addListener(SetChangeListener<? super T> listener)
	{

	}
	@Override public void removeListener(SetChangeListener<? super T> listener)
	{

	}
	@Override public void addListener(InvalidationListener listener)
	{

	}
	@Override public void removeListener(InvalidationListener listener)
	{

	}
	@Override public int size()
	{
		return 0;
	}
	@Override public boolean isEmpty()
	{
		return false;
	}
	@Override public boolean contains(Object o)
	{
		return false;
	}
	@Override public Iterator<T> iterator()
	{
		return null;
	}
	@Override public Object[] toArray()
	{
		return new Object[0];
	}
	@Override public <T1> T1[] toArray(T1[] a)
	{
		return null;
	}
	@Override public boolean add(T t)
	{
		return false;
	}
	@Override public boolean remove(Object o)
	{
		return false;
	}
	@Override public boolean containsAll(Collection<?> c)
	{
		return false;
	}
	@Override public boolean addAll(Collection<? extends T> c)
	{
		return false;
	}
	@Override public boolean retainAll(Collection<?> c)
	{
		return false;
	}
	@Override public boolean removeAll(Collection<?> c)
	{
		return false;
	}
	@Override public void clear()
	{

	}
}
