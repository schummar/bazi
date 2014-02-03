package de.uni_augsburg.bazi.cl;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import de.uni_augsburg.bazi.common.Json;
import de.uni_augsburg.bazi.common.Resources;
import de.uni_augsburg.bazi.monoprop.DivisorMethod;
import de.uni_augsburg.bazi.monoprop.DivisorOutput;
import de.uni_augsburg.bazi.monoprop.MonopropInput;
import de.uni_augsburg.bazi.monoprop.MonopropOutput;

import java.lang.reflect.Type;
import java.util.List;

@Json.Deserialize(Method.Deserializer.class)
interface Method
{
	public static class Deserializer implements JsonDeserializer<Method>
	{
		@Override
		public Method deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
		{
			return null;
		}
	}


	public static StringTable asStringTable(List<Output> outputs, Options.Orientation orientation, Options.DivisorFormat divisorFormat, Options.TieFormat tieFormat)
	{
		return null;
	}

	public interface Output
	{
		public MonopropOutput get();
		public String label();

		public default StringTable asStringTable(Options.Orientation orientation, Options.DivisorFormat divisorFormat, Options.TieFormat tieFormat)
		{
			StringTable st = new StringTable();
			// ...
			return st;
		}
	}


	public static class Divisor implements Method
	{
		private final DivisorMethod method;

		public Divisor(DivisorMethod method)
		{
			this.method = method;
		}

		public Output calculate(MonopropInput input)
		{
			return new Output(method.calculate(input));
		}


		public class Output implements Method.Output
		{
			private final DivisorOutput output;

			public Output(DivisorOutput output)
			{
				this.output = output;
			}

			@Override
			public MonopropOutput get()
			{
				return output;
			}

			@Override
			public String label()
			{
				return Resources.get("output.divisor");
			}
		}
	}
}
