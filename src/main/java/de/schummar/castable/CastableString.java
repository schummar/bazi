package de.schummar.castable;

import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleStringProperty;

public class CastableString extends SimpleStringProperty implements Castable
{
	public CastableString()
	{
		super();
	}
	public CastableString(String initialValue)
	{
		super(initialValue);
	}


	@Override public void addDeepListener(InvalidationListener invalidationListener)
	{
		addListener(invalidationListener);
	}
	@Override public void removeDeepListener(InvalidationListener invalidationListener)
	{
		removeListener(invalidationListener);
	}


	@Override public Castable copy()
	{
		return new CastableString(getValue());
	}
	@Override public void overwrite(Castable castable)
	{
		setValue(castable.asCastableString().getValue());
	}
	@Override public void merge(Castable castable)
	{
		setValue(castable.asCastableString().getValue());
	}

	@Override public boolean isCastableString() { return true; }
	@Override public CastableString asCastableString() { return this; }


	@Override public String toString()
	{
		return getValue();
	}
}
