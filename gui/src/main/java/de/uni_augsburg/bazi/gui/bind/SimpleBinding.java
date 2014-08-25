package de.uni_augsburg.bazi.gui.bind;

import javafx.beans.Observable;
import javafx.beans.binding.ObjectBinding;

import java.util.function.Function;

public class SimpleBinding
{
	public static <T extends Observable, S> ObjectBinding<S> of(T value, Function<T, S> function)
	{
		return new ObjectBinding<S>()
		{
			{ bind(value); }
			@Override protected S computeValue()
			{
				return function.apply(value);
			}
		};
	}
}
