package de.schummar.castable;

import javafx.beans.InvalidationListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.*;

public class CList<T> extends AbstractList<T> implements ObservableList<T>
{
	protected final CastableList list;
	protected final Converter<T> converter;
	protected final List<InvalidationListener> invalidationListeners = new ArrayList<>();
	protected final List<ListChangeListener<? super T>> listChangeListeners = new ArrayList<>();
	public CList(CastableList list, Converter<T> converter)
	{
		this.list = list;
		this.converter = converter;
		list.addListener(invalidationListener);
		list.addListener(listChangeListener);
	}

	private InvalidationListener invalidationListener = observable -> new ArrayList<>(invalidationListeners).forEach(listener -> listener.invalidated(this));
	private ListChangeListener<? super Castable<?>> listChangeListener = change -> {
		ListChangeListener.Change<T> tChange = new ListChangeListener.Change<T>(this)
		{
			@Override public boolean next() { return change.next(); }
			@Override public void reset() { change.reset(); }
			@Override public int getFrom() { return change.getFrom(); }
			@Override public int getTo() { return change.getTo(); }
			@Override public List<T> getRemoved()
			{
				List<T> removed = new ArrayList<>();
				change.getRemoved().forEach(c -> removed.add(converter.apply(c)));
				return removed;
			}
			@Override protected int[] getPermutation()
			{
				if (!change.wasPermutated()) return new int[0];
				int[] permutation = new int[getTo() - getFrom()];
				for (int i = getFrom(); i < getTo(); i++)
					permutation[i] = change.getPermutation(i + getFrom());
				return permutation;
			}
		};
		new ArrayList<>(listChangeListeners).forEach(l -> l.onChanged(tChange));
	};


	@Override public void addListener(ListChangeListener<? super T> listener)
	{
		listChangeListeners.add(listener);
	}
	@Override public void removeListener(ListChangeListener<? super T> listener)
	{
		listChangeListeners.remove(listener);
	}
	@Override @SafeVarargs public final boolean addAll(T... elements)
	{
		return addAll(Arrays.asList(elements));
	}
	@Override @SafeVarargs public final boolean setAll(T... elements)
	{
		return setAll(Arrays.asList(elements));
	}
	@Override public boolean setAll(Collection<? extends T> col)
	{
		boolean empty = isEmpty();
		clear();
		return addAll(col) || !empty;
	}
	@Override @SafeVarargs public final boolean removeAll(T... elements)
	{
		return removeAll(Arrays.asList(elements));
	}
	@Override @SafeVarargs public final boolean retainAll(T... elements)
	{
		return retainAll(Arrays.asList(elements));
	}
	@Override public void remove(int from, int to)
	{
		for (int i = from; i < to; i++)
			remove(from);
	}
	@Override public T get(int index)
	{
		return converter.apply(list.get(index));
	}
	@Override public int size()
	{
		return list.size();
	}
	@Override public void addListener(InvalidationListener listener)
	{
		invalidationListeners.add(listener);
	}
	@Override public void removeListener(InvalidationListener listener)
	{
		invalidationListeners.remove(listener);
	}
	@Override public T set(int index, T element)
	{
		return converter.apply(list.set(index, converter.applyInverse(element)));
	}
	@Override public void add(int index, T element)
	{
		list.add(index, converter.applyInverse(element));
	}
	@Override public T remove(int index)
	{
		return converter.apply(list.remove(index));
	}

	public T addNew()
	{
		add(converter.apply(null));
		return last();
	}
	public T last()
	{
		return get(size() - 1);
	}
	public <S> CList<S> cast(Class<S> c)
	{
		return new CList<>(list, Converters.get(c));
	}
}
