package de.schummar.castable;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.util.ArrayList;
import java.util.List;

public class CastBinding<T> implements Property<T>, InvalidationListener
{
	private final Castable<?> castable;
	private final Converter<T> converter;
	private final List<ChangeListener<? super T>> changeListeners = new ArrayList<>();
	private final List<InvalidationListener> invalidationListeners = new ArrayList<>();
	private T value = null;
	public CastBinding(Castable castable, Converter<T> converter)
	{
		this.castable = castable;
		this.converter = converter;
		castable.addDeepListener(this);
	}


	@Override public void invalidated(Observable observable)
	{
		T oldValue = value;
		value = null;
		informListeners(oldValue);
	}
	private void informListeners(T oldValue)
	{
		changeListeners.forEach(l -> l.changed(this, oldValue, getValue()));
		invalidationListeners.forEach(l -> l.invalidated(this));
	}


	@Override public void bind(ObservableValue<? extends T> observable)
	{
		throw new RuntimeException("cannot bind an CastBinding");
	}
	@Override public void unbind()
	{
		castable.removeListener(this);
	}
	@Override public boolean isBound()
	{
		return true;
	}
	@Override public void bindBidirectional(Property<T> other)
	{
		Bindings.bindBidirectional(this, other);
	}
	@Override public void unbindBidirectional(Property<T> other)
	{
		Bindings.unbindBidirectional(this, other);
	}
	@Override public Object getBean()
	{
		return null;
	}
	@Override public String getName()
	{
		return "";
	}
	@Override public void addListener(ChangeListener<? super T> listener)
	{
		changeListeners.add(listener);
	}
	@Override public void removeListener(ChangeListener<? super T> listener)
	{
		changeListeners.remove(listener);
	}
	@Override public T getValue()
	{
		if (value == null) value = converter.apply(castable);
		return value;
	}
	@Override public void addListener(InvalidationListener listener)
	{
		invalidationListeners.add(listener);
	}
	@Override public void removeListener(InvalidationListener listener)
	{
		invalidationListeners.remove(listener);
	}
	@Override public void setValue(T value)
	{
		castable.overwrite(converter.applyInverse(value));
		T oldValue = this.value;
		this.value = value;
		informListeners(oldValue);
	}

	@Override public String toString()
	{
		return getValue().toString();
	}
}
