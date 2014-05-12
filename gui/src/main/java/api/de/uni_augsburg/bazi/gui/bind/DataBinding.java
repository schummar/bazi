package de.uni_augsburg.bazi.gui.bind;

import de.uni_augsburg.bazi.common.data.Data;
import de.uni_augsburg.bazi.common.data.MapData;
import javafx.application.Platform;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.value.WritableObjectValue;

public class DataBinding<T> extends ObjectBinding<T> implements WritableObjectValue<T>
{
	private final MapData data;
	private final String name;
	public DataBinding(Data data, String name)
	{
		if (name.equals("votes")) System.out.println("abc");
		this.data = data.toMapData();
		this.name = name;
	}

	@Override protected void onInvalidating()
	{
		super.onInvalidating();
	}
	@Override protected T computeValue()
	{
		@SuppressWarnings("unchecked")
		T t = (T) data.get(name);
		if (name.equals("votes")) Platform.runLater(() -> System.out.println("compute: " + this));
		return t;
	}
	@Override public void set(T value)
	{
		data.put(name, value);
		invalidate();
		if (name.equals("votes")) System.out.println("set: " + this);
		if (name.equals("votes")) Platform.runLater(() -> System.out.println("set: " + this));
	}
	@Override public void setValue(T value)
	{
		set(value);
	}
}
