package de.schummar.castable;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;

public interface Castable extends Observable
{
	Castable copy();
	void overwrite(Castable castable);
	void addDeepListener(InvalidationListener invalidationListener);
	void removeDeepListener(InvalidationListener invalidationListener);

	default CastableString asCastableString() { throw new RuntimeException(this + " is no string"); }
	default CastableList asCastableList() { throw new RuntimeException(this + " is no list"); }
	default CastableObject asCastableObject() { throw new RuntimeException(this + " is no object"); }
}
