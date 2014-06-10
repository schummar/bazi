package de.uni_augsburg.bazi.gui.bind;

import javafx.beans.binding.ObjectBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.function.BinaryOperator;

public class ReductionBinding<T> extends ObjectBinding<T>
{
	private final T neutral;
	private final BinaryOperator<T> op, invOp;
	private T value;
	public ReductionBinding(ObservableList<ObservableValue<T>> list, T neutral, BinaryOperator<T> op, BinaryOperator<T> invOp)
	{
		this.neutral = neutral;
		this.op = op;
		this.invOp = invOp;
		value = neutral;

		list.addListener(listChangeListener);
		list.forEach(
			t -> {
				t.addListener(changeListener);
				changeListener.changed(t, neutral, t.getValue());
			}
		);
	}
	private T neutral() { return neutral; }
	private BinaryOperator<T> op() { return op; }
	private BinaryOperator<T> invOp() { return invOp; }
	private ChangeListener<T> changeListener = (observableValue, oldValue, newValue) -> {
		value = invOp().apply(value, oldValue);
		value = op().apply(value, newValue);
	};
	private final ListChangeListener<ObservableValue<T>> listChangeListener = change -> {
		while (change.next())
		{
			change.getRemoved().forEach(
				t -> {
					t.removeListener(changeListener);
					changeListener.changed(t, t.getValue(), neutral());
				}
			);
			change.getAddedSubList().forEach(
				t -> {
					t.addListener(changeListener);
					changeListener.changed(t, neutral(), t.getValue());
				}
			);
		}
	};


	@Override protected T computeValue()
	{
		return value;
	}
}
