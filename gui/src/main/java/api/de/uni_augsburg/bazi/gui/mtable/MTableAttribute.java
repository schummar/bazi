package de.uni_augsburg.bazi.gui.mtable;

import javafx.beans.value.ObservableValue;

import java.util.function.BinaryOperator;
import java.util.function.Function;

public interface MTableAttribute<T, S>
{
	public static <T> MTableAttribute<T, String> create(Function<T, ObservableValue<String>> extractor)
	{
		return create(extractor, Function.identity(), Function.identity(), null, "");
	}

	static <T, S> MTableAttribute<T, S> create(
		Function<T, ObservableValue<S>> extractor,
		Function<S, String> serializer,
		Function<String, S> deserializer,
		BinaryOperator<S> addition,
		S neutral)
	{
		return new MTableAttribute<T, S>()
		{
			@Override public Function<T, ObservableValue<S>> extractor() { return extractor; }
			@Override public Function<S, String> serializer() { return serializer; }
			@Override public Function<String, S> deserializer() { return deserializer; }
			@Override public BinaryOperator<S> addition() { return addition; }
			@Override public S neutral() { return neutral; }
		};
	}


	Function<T, ObservableValue<S>> extractor();
	Function<S, String> serializer();
	Function<String, S> deserializer();
	BinaryOperator<S> addition();
	S neutral();
	default MTableAttribute<T, S> nullSafe() { return new NullSafe<>(this); }


	static class NullSafe<T, S> implements MTableAttribute<T, S>
	{
		private final Function<T, ObservableValue<S>> extractor;
		private final Function<S, String> serializer;
		private final Function<String, S> deserializer;
		private final BinaryOperator<S> addition;
		private final S neutral;
		public NullSafe(MTableAttribute<T, S> attribute)
		{
			extractor = attribute.extractor();
			serializer = x -> x == null ? "" : attribute.serializer().apply(x);
			deserializer = s -> s == null ? neutral() : attribute.deserializer().apply(s);
			if (attribute.addition() != null)
				addition = (x, y) -> x == null ? y : (y == null ? x : attribute.addition().apply(x, y));
			else
				addition = null;
			neutral = attribute.neutral();
		}
		@Override public Function<T, ObservableValue<S>> extractor() { return extractor; }
		@Override public Function<S, String> serializer() { return serializer; }
		@Override public Function<String, S> deserializer() { return deserializer; }
		@Override public BinaryOperator<S> addition() { return addition; }
		@Override public S neutral() { return neutral; }
		@Override public MTableAttribute<T, S> nullSafe() { return this; }
	}
}
