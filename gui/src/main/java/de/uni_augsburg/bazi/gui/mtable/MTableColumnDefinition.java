package de.uni_augsburg.bazi.gui.mtable;

import javafx.beans.property.Property;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;

import java.util.function.BinaryOperator;
import java.util.function.Function;

public interface MTableColumnDefinition<T, S>
{
	public static <T> MTableColumnDefinition<T, String> create(
		ObservableValue<String> title,
		Function<T, Property<String>> attribute,
		Pos alignment)
	{
		return create(title, attribute, attribute, null, null, null, alignment);
	}

	public static <T, S> MTableColumnDefinition<T, S> create(
		ObservableValue<String> title,
		Function<T, Property<String>> stringAttribute,
		Function<T, Property<S>> attribute,
		BinaryOperator<S> op,
		BinaryOperator<S> invOp,
		S def,
		Pos alignment)
	{
		return new MTableColumnDefinition<T, S>()
		{
			@Override public ObservableValue<String> title() { return title; }
			@Override public Function<T, Property<String>> stringAttribute() { return stringAttribute; }
			@Override public Function<T, Property<S>> attribute() { return attribute; }
			@Override public BinaryOperator<S> op() { return op; }
			@Override public BinaryOperator<S> invOp() { return invOp; }
			@Override public S def() { return def; }
			@Override public Pos alignment() { return alignment; }
		};
	}


	ObservableValue<String> title();
	Function<T, Property<String>> stringAttribute();
	Function<T, Property<S>> attribute();
	BinaryOperator<S> op();
	BinaryOperator<S> invOp();
	S def();
	Pos alignment();

	default MTableColumn<T, S> createColumn() { return new MTableColumn<>(this); }
}
