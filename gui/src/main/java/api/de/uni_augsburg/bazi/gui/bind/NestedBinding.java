package de.uni_augsburg.bazi.gui.bind;

import javafx.beans.binding.ObjectBinding;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;

import java.util.function.Function;

public abstract class NestedBinding<T> extends ObjectBinding<T>
{
	public static <T> NestedBinding<T> of(ObservableValue<T> root)
	{
		return new Impl<>(root, v -> root);
	}

	public static <T, S> NestedBinding<S> of(ObservableValue<T> root, Function<T, ObservableValue<S>> extractor)
	{
		return new Impl<>(root, extractor);
	}

	public <S> NestedBinding<S> property(Function<T, ObservableValue<S>> extractor)
	{
		return new Impl<>(this, extractor);
	}

	public <S> NestedListBinding<S> listProperty(Function<T, ObservableValue<ObservableList<S>>> extractor)
	{
		return NestedListBinding.of(property(extractor));
	}


	private static class Impl<T, D> extends NestedBinding<T>
	{
		private final ObservableValue<D> dependency;
		private final Function<D, ObservableValue<T>> extractor;
		private ObservableValue<T> value = null;
		private Impl(ObservableValue<D> dependency, Function<D, ObservableValue<T>> extractor)
		{
			this.dependency = dependency;
			this.extractor = extractor;
			bind(dependency);
		}
		@Override protected T computeValue()
		{
			if (dependency.getValue() == null) return null;

			if (value != null) unbind(value);
			bind(value = extractor.apply(dependency.getValue()));

			return value.getValue();
		}
	}
}
