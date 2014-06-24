package de.schummar.castable;

import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class CProperty<T> implements Property<T>
{
	protected final Castable castable;
	protected final Converter<T> converter;
	protected final Function<String, String> validator;
	protected final List<ChangeListener<? super T>> changeListeners = new ArrayList<>();
	protected final List<InvalidationListener> invalidationListeners = new ArrayList<>();
	protected T value = null;
	protected boolean valid = false;
	public CProperty(Castable castable, Converter<T> converter, Function<String, String> validator)
	{
		this.castable = castable;
		this.converter = converter;
		this.validator = validator;
		castable.addDeepListener(invalidationListener);
	}

	protected final InvalidationListener invalidationListener = observable ->
	{
		if (!valid) return;
		T oldValue = getValue();
		valid = false;
		informListeners(oldValue);
	};
	protected void informListeners(T oldValue)
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
		if (!valid)
		{
			value = converter.unpack(castable);
			valid = true;
		}
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
		T oldValue = getValue();
		if (Objects.equals(oldValue, value)) return;

		valid = false;
		castable.overwrite(converter.pack(value));
		this.value = value;
		valid = true;
		informListeners(oldValue);
	}
	@Override public String toString()
	{
		return String.valueOf(getValue());
	}
	private Property<String> stringProperty = null;
	public Property<String> asStringProperty()
	{
		if (stringProperty == null)
			stringProperty = new CProperty<String>(castable, Converters.STRING_OBJ_CONVERTER, validator)
			{
				@Override public void setValue(String value)
				{
					String oldValue = getValue();
					if (Objects.equals(oldValue, value)) return;

					value = validator.apply(value);
					valid = false;
					castable.overwrite(converter.pack(value));
					this.value = value;
					valid = true;
					informListeners(oldValue);
				}
			};

		return stringProperty;
	}
}
