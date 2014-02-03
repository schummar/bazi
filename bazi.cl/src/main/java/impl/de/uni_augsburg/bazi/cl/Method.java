package de.uni_augsburg.bazi.cl;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import de.uni_augsburg.bazi.common.Json;
import de.uni_augsburg.bazi.common.Resources;
import de.uni_augsburg.bazi.monoprop.*;

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

		public String quotient(MonopropOutput.Party party);
		public String divquo(Options.DivisorFormat divisorFormat);
		public String name();

		public default StringTable asStringTable(Options.Orientation orientation, Options.DivisorFormat divisorFormat, Options.TieFormat tieFormat)
		{
			StringTable st = new StringTable();
			st.append(quotientColumn(divisorFormat));
			st.append(resultColumn(divisorFormat, tieFormat));
			return st;
		}

		public default StringTable quotientColumn(Options.DivisorFormat divisorFormat)
		{
			StringTable st = new StringTable();
			StringTable.Column col = st.col(0);
			int row = 0;

			col.set(row++, Resources.get("output.quotient"));

			for (MonopropOutput.Party party : get().parties())
				col.set(row++, quotient(party));

			col.set(row++, divquo(divisorFormat));

			return st;
		}

		public default StringTable resultColumn(Options.DivisorFormat divisorFormat, Options.TieFormat tieFormat)
		{
			StringTable st = new StringTable();
			StringTable.Column col = st.col(0);
			int row = 0;

			col.set(row++, name());

			for (MonopropOutput.Party party : get().parties())
			{
				String s = party.seats().toString();
				if (party.uniqueness() == Uniqueness.CAN_BE_MORE)
					s += Resources.get("output.can_be_more");
				else if (party.uniqueness() == Uniqueness.CAN_BE_LESS)
					s += Resources.get("output.can_be_more");

				col.set(row++, s);
			}

			if (divisorFormat != Options.DivisorFormat.QUOTIENT)
				col.set(row++, divquo(divisorFormat));

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
			public String quotient(MonopropOutput.Party party)
			{
				return party.votes().div(output.divisor().getNice()).toString();
			}

			@Override
			public String divquo(Options.DivisorFormat divisorFormat)
			{
				switch (divisorFormat)
				{
					case DIV_INTERVAL:
						return String.format("[%s..%s]", output.divisor().getMin(), output.divisor().getMax());
					case MULT:
						return output.divisor().getNice().inv().toString();
					case MULT_INTERVAL:
						return String.format("[%s..%s]", output.divisor().getMax().inv(), output.divisor().getMin().inv());
					case DIV_QUO:
					case QUOTIENT:
					default:
						return output.divisor().getNice().toString();
				}
			}

			@Override
			public String name()
			{
				return method.getClass().toString();
			}
		}
	}
}
