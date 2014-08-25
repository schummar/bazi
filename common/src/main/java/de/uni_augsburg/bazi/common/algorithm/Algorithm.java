package de.uni_augsburg.bazi.common.algorithm;

import de.schummar.castable.*;
import de.uni_augsburg.bazi.common.Plugin;
import de.uni_augsburg.bazi.common.StringTable;
import de.uni_augsburg.bazi.common.plain.PlainOptions;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.function.BiFunction;

/** An algorithm that in some way calculates seat apportionments out of votes and other input data. */
@Convert(Algorithm.Converter.class)
public interface Algorithm<T extends Data> extends Plugin.Instance
{
	/**
	 * The display name of this algorithm.
	 * @return the display name of this algorithm.
	 */
	String name();
	Class<T> dataType();
	default Algorithm<T> unwrap() { return this; }

	BiFunction<Data, PlainOptions, List<StringTable>> plainFormatter();

	/**
	 * Applies this algorithm to the given input.
	 * @param data input
	 * @param options general calculation options
	 */
	void apply(Data data, Options options);


	class Converter implements de.schummar.castable.Converter<Algorithm>
	{
		private final Class<? extends Data> dataType;
		public Converter(Type type)
		{
			Type t;
			if (type instanceof ParameterizedType)
				t = ((ParameterizedType) type).getActualTypeArguments()[0];
			else t = Data.class;
			@SuppressWarnings("unchecked")
			Class<? extends Data> dataType = (Class<? extends Data>) Converters.classOf(t);
			if (!Data.class.isAssignableFrom(dataType)) dataType = Data.class;
			this.dataType = dataType;
		}
		@Override public Algorithm unpack(Castable castable)
		{
			CastableObject data;
			if (castable.isCastableObject()) data = castable.asCastableObject();
			else if (castable.isCastableString())
			{
				data = new CastableObject();
				data.cast(Plugin.Params.class).name(castable.asCastableString().getValue());
			}
			else data = new CastableObject();
			return AlgorithmManager.tryInstantiate(dataType, data).orElse(null);
		}
		@Override public Castable pack(Algorithm t)
		{
			return new CastableString(t == null ? null : t.toString());
		}
	}
}
