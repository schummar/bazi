package de.uni_augsburg.bazi.gui.bind;

import javafx.beans.binding.ObjectBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.function.BinaryOperator;

public class ReductionBinding<T> extends ObjectBinding<T>
{
	private final BinaryOperator<T> op, invOp;
	private T value = null;
	public ReductionBinding(ObservableList<ObservableValue<T>> list, BinaryOperator<T> op, BinaryOperator<T> invOp)
	{
		this.op = op;
		this.invOp = invOp;

		list.addListener(listChangeListener);
		list.forEach(
			t -> {
				t.addListener(changeListener);
				changeListener.changed(t, null, t.getValue());
			}
		);
	}
	private BinaryOperator<T> op() { return op; }
	private BinaryOperator<T> invOp() { return invOp; }
	private ChangeListener<T> changeListener = (observableValue, oldValue, newValue) -> {
		if (value == null) value = newValue;
		if (oldValue != null) value = invOp().apply(value, oldValue);
		if (newValue != null) value = op().apply(value, newValue);
		invalidate();
	};
	private final ListChangeListener<ObservableValue<T>> listChangeListener = change -> {
		while (change.next())
		{
			change.getRemoved().forEach(
				t -> {
					t.removeListener(changeListener);
					changeListener.changed(t, t.getValue(), null);
				}
			);
			change.getAddedSubList().forEach(
				t -> {
					t.addListener(changeListener);
					changeListener.changed(t, null, t.getValue());
				}
			);
		}
	};


	@Override protected T computeValue()
	{
		return value;
	}
}
