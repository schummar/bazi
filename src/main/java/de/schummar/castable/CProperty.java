package de.schummar.castable;

import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.util.ArrayList;
import java.util.List;

public class CProperty<T> implements Property<T>
{
	private final Castable castable;
	private final Converter<T> converter;
	private final T def;
	private final List<ChangeListener<? super T>> changeListeners = new ArrayList<>();
	private final List<InvalidationListener> invalidationListeners = new ArrayList<>();
	private T value = null;
	private boolean invalid = true;
	public CProperty(Castable castable, Converter<T> converter, T def)
	{
		this.castable = castable;
		this.converter = converter;
		this.def = def;
		castable.addDeepListener(invalidationListener);
	}

	private void invalidate()
	{
		invalid = true;
	}


	private final InvalidationListener invalidationListener = observable ->
	{
		if (invalid) return;
		T oldValue = getValue();
		invalidate();
		informListeners(oldValue);
	};
	private void informListeners(T oldValue)
	{
		changeListeners.forEach(l -> l.changed(this, oldValue, getValue()));
		invalidationListeners.forEach(l -> l.invalidated(this));
	}


	@Override public void bind(ObservableValue<? extends T> observable)
	{
		throw new RuntimeException("cannot bind a CProperty");
	}
	@Override public void unbind()
	{
		castable.removeListener(invalidationListener);
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
		if (invalid) value = converter.unpack(castable);
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
		castable.overwrite(converter.pack(value));
		T oldValue = this.value;
		this.value = value;
		informListeners(oldValue);
	}
	@Override public String toString()
	{
		return String.valueOf(getValue());
	}
	public Property<String> asStringProperty()
	{
		return new CProperty<String>(castable, Converters.STRING_OBJ_CONVERTER, null)
		{
			@Override public void setValue(String value)
			{
				if (value != null) try
				{
					T t = converter.unpack(new CastableString(value));
					CProperty.this.setValue(t);
					return;
				}
				catch (Exception ignore) { }
				CProperty.this.setValue(def);
			}
		};
	}
}
