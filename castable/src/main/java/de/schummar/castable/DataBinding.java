package de.schummar.castable;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;

public class DataBinding implements InvalidationListener
{
	public static void bind(Data a, Data b)
	{
		a.src().overwrite(b.src());
		new DataBinding(a, b).on();
	}
	public static void unbind(Data a, Data b)
	{
		new DataBinding(a, b).off();
	}

	private final Data a, b;
	private DataBinding(Data a, Data b)
	{
		this.a = a;
		this.b = b;
	}

	private void on()
	{
		a.src().addDeepListener(this);
		b.src().addDeepListener(this);
	}
	private void off()
	{
		a.src().removeDeepListener(this);
		b.src().removeDeepListener(this);
	}

	@Override public void invalidated(Observable observable)
	{
		if (observable == a.src()) b.src().overwrite(a.src());
		else if (observable == b.src()) a.src().overwrite(b.src());
	}

	@Override public boolean equals(Object obj)
	{
		if (!(obj instanceof DataBinding)) return false;
		DataBinding that = (DataBinding) obj;
		return (a.equals(that.a) && b.equals(that.b))
			|| (a.equals(that.b) && b.equals(that.a));
	}
	@Override public int hashCode()
	{
		return a.hashCode() ^ b.hashCode();
	}
}
