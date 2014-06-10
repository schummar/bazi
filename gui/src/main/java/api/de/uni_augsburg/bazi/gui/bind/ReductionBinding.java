package de.uni_augsburg.bazi.gui.bind;

import javafx.beans.binding.ObjectBinding;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.function.BinaryOperator;

public class ReductionBinding<T> extends ObjectBinding<T>
{
	private final ObservableList<ObservableValue<T>> dep;
	private final BinaryOperator<T> operator;
	public ReductionBinding(ObservableList<ObservableValue<T>> dep, BinaryOperator<T> operator)
	{
		this.dep = dep;
		this.operator = operator;
		bind(dep);
		dep.forEach(this::bind);
	}
	private ListChangeListener<ObservableValue<T>> listChangeListener = change -> {
		while (change.next())
		{
			change.getRemoved().forEach(this::unbind);
			change.getAddedSubList().forEach(this::bind);
		}
	};


	@Override protected T computeValue()
	{
		return dep.stream()
			.map(ObservableValue::getValue)
			.reduce(operator)
			.orElse(null);
	}
}
