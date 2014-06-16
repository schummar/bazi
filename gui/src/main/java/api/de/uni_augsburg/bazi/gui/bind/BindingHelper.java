package de.uni_augsburg.bazi.gui.bind;

import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ListExpression;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BindingHelper
{
	public static <E> ObservableList<E> listBinding(ObservableValue<ObservableList<E>> listObservableValue)
	{
		return new ListBinding<>(listObservableValue);
	}


	private static class ListBinding<E> extends ListExpression<E>
	{
		private final ObservableValue<ObservableList<E>> dep;
		private final IntegerProperty sizeProperty = new SimpleIntegerProperty(0);
		private final BooleanProperty emptyProperty = new SimpleBooleanProperty(true);
		private final List<InvalidationListener> invalidationListeners = new ArrayList<>();
		private final List<ChangeListener<? super ObservableList<E>>> changeListeners = new ArrayList<>();
		private final List<ListChangeListener<? super E>> listChangeListeners = new ArrayList<>();
		private ListBinding(ObservableValue<ObservableList<E>> dep)
		{
			this.dep = dep;
			dep.addListener(changeListener);
		}

		private final ListChangeListener<? super E> listChangeListener = change -> {
			new ArrayList<>(listChangeListeners).forEach(l -> l.onChanged(change));
			new ArrayList<>(invalidationListeners).forEach(l -> l.invalidated(this));
		};
		private final ChangeListener<? super ObservableList<E>> changeListener = (observableValue, oldValue, newValue) -> {
			if (oldValue != null)
			{
				oldValue.removeListener(listChangeListener);
			}
			if (newValue != null)
			{
				newValue.addListener(listChangeListener);
				sizeProperty.bind(Bindings.size(newValue));
				emptyProperty.bind(Bindings.isEmpty(newValue));
			}
			else
			{
				sizeProperty.unbind();
				sizeProperty.setValue(0);
				emptyProperty.unbind();
				emptyProperty.setValue(true);
			}
			listChangeListener.onChanged(
				new ListChangeListener.Change<E>(this)
				{
					private int step = 0;
					@Override public boolean next() { return step++ < 2; }
					@Override public void reset() { step = 0;}
					@Override public int getFrom() { return 0; }
					@Override public int getTo()
					{
						return step == 1
							? (oldValue == null ? 0 : oldValue.size())
							: (newValue == null ? 0 : newValue.size());
					}
					@Override public List<E> getRemoved() { return step == 1 && oldValue != null ? oldValue : Collections.emptyList(); }
					@Override protected int[] getPermutation() { return new int[0]; }
				}
			);
			new ArrayList<>(changeListeners).forEach(l -> l.changed(this, oldValue, newValue));
		};

		@Override public ReadOnlyIntegerProperty sizeProperty()
		{
			return sizeProperty;
		}
		@Override public ReadOnlyBooleanProperty emptyProperty()
		{
			return emptyProperty;
		}
		@Override public void addListener(ListChangeListener<? super E> listener)
		{
			listChangeListeners.add(listener);
		}
		@Override public void removeListener(ListChangeListener<? super E> listener)
		{
			listChangeListeners.remove(listener);
		}
		@Override public ObservableList<E> get()
		{
			return dep.getValue();
		}
		@Override public void addListener(ChangeListener<? super ObservableList<E>> listener)
		{
			changeListeners.add(listener);
		}
		@Override public void removeListener(ChangeListener<? super ObservableList<E>> listener)
		{
			changeListeners.remove(listener);
		}
		@Override public void addListener(InvalidationListener listener)
		{
			invalidationListeners.add(listener);
		}
		@Override public void removeListener(InvalidationListener listener)
		{
			invalidationListeners.remove(listener);
		}
		@Override public String toString()
		{
			return getValue() == null ? super.toString() : getValue().toString();
		}
	}
}
