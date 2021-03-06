package de.schummar.castable;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;

public interface Castable extends Observable
{
	Castable copy();
	void merge(Castable castable);

	void overwrite(Castable castable);
	void addDeepListener(InvalidationListener invalidationListener);
	void removeDeepListener(InvalidationListener invalidationListener);

	default boolean isCastableString() { return false; }
	default CastableString asCastableString() { throw new CastException(this + " is no string"); }
	default boolean isCastableList() { return false; }
	default CastableList asCastableList() { throw new CastException(this + " is no list"); }
	default boolean isCastableObject() { return false; }
	default CastableObject asCastableObject() { throw new CastException(this + " is no object"); }


	public static class CastException extends RuntimeException
	{
		public CastException(String message)
		{
			super(message);
		}
	}
}
