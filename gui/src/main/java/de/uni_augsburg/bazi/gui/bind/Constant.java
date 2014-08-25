package de.uni_augsburg.bazi.gui.bind;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class Constant<T> implements ObservableValue<T>
{
	public static <T> Constant<T> of(T value) { return new Constant<>(value); }


	private final T value;
	public Constant(T value)
	{
		this.value = value;
	}
	@Override public T getValue() { return value; }


	@Override public void addListener(ChangeListener<? super T> listener) { }
	@Override public void removeListener(ChangeListener<? super T> listener) {}
	@Override public void addListener(InvalidationListener listener) { }
	@Override public void removeListener(InvalidationListener listener) { }
}
