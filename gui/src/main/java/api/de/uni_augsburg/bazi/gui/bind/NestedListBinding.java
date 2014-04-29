package de.uni_augsburg.bazi.gui.bind;

import javafx.beans.binding.ListBinding;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class NestedListBinding<T> extends ListBinding<T>
{
	public static <T> NestedListBinding<T> of(ObservableList<T> list)
	{
		return new Mapping<>(list, Function.identity());
	}

	public static <T> NestedListBinding<T> of(ObservableValue<ObservableList<T>> list)
	{
		return new Mapping<>(
			new ListBinding<T>()
			{
				{ bind(list); }
				@Override protected ObservableList<T> computeValue()
				{
					return list.getValue();
				}
			}, Function.identity()
		);
	}

	public static <T, S> NestedListBinding<S> of(ObservableList<T> list, Function<T, S> mapper)
	{
		return new Mapping<>(list, mapper);
	}

	public <S> NestedListBinding<S> map(Function<T, S> mapper)
	{
		return new Mapping<>(this, mapper);
	}

	public NestedBinding<T> reduce(BinaryOperator<T> aggregator)
	{
		return NestedBinding.of(
			this,
			olist -> SimpleBinding.of(
				olist,
				list -> list.stream().reduce(aggregator).orElse(null)
			)
		);
	}

	private static class Mapping<T, D> extends NestedListBinding<T>
	{
		private final ObservableList<D> dependecy;
		private final Function<D, T> mapper;
		private Mapping(ObservableList<D> dependecy, Function<D, T> mapper)
		{
			this.dependecy = dependecy;
			this.mapper = mapper;
			bind(dependecy);
		}
		@Override protected ObservableList<T> computeValue()
		{
			return dependecy.stream()
				.map(mapper)
				.collect(Collectors.toCollection(FXCollections::observableArrayList));
		}
	}
}
