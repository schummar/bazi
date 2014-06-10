package de.uni_augsburg.bazi.gui.mtable;

import de.schummar.castable.CProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;

import java.util.function.BinaryOperator;
import java.util.function.Function;

public interface MTableColumnDefinition<T, S>
{
	public static <T> MTableColumnDefinition<T, String> create(
		ObservableValue<String> title,
		Function<T, CProperty<String>> attribute)
	{
		return create(title, attribute, null, null, null, Pos.CENTER_LEFT);
	}

	public static <T, S> MTableColumnDefinition<T, S> create(
		ObservableValue<String> title,
		Function<T, CProperty<S>> attribute,
		BinaryOperator<S> op,
		BinaryOperator<S> invOp,
		S neutral,
		Pos alignment)
	{
		return new MTableColumnDefinition<T, S>()
		{
			@Override public ObservableValue<String> title() { return title; }
			@Override public Function<T, CProperty<S>> attribute() { return attribute; }
			@Override public BinaryOperator<S> op() { return op; }
			@Override public BinaryOperator<S> invOp() { return invOp; }
			@Override public S neutral() { return neutral; }
			@Override public Pos alignment() { return alignment; }
		};
	}


	ObservableValue<String> title();
	Function<T, CProperty<S>> attribute();
	BinaryOperator<S> op();
	BinaryOperator<S> invOp();
	S neutral();
	Pos alignment();

	default MTableColumn<T, S> createColumn() { return new MTableColumn<>(this); }
}
