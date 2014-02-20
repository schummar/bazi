package de.uni_augsburg.bazi.cl;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import de.uni_augsburg.bazi.common.Json;
import de.uni_augsburg.bazi.common.Resources;
import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Rational;
import de.uni_augsburg.bazi.monoprop.*;

import java.lang.reflect.Type;

@Json.Deserialize(BasicMethod.Deserializer.class)
interface BasicMethod
{
	public static class Deserializer implements JsonDeserializer<BasicMethod>
	{
		@Override
		public BasicMethod deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
		{
			return null;
		}
	}

	public Output calculate(MonopropInput input);


	public interface Output
	{
		public BasicMethod method();
		public MonopropOutput output();
		public MonopropOutput.Party findParty(MonopropInput.Party party);
		public Output outputFor(MonopropInput.Party party);

		public StringTable quotientColumn(Options.DivisorFormat divisorFormat);
		public String divquo(Options.DivisorFormat divisorFormat);
		public String name();

		public default StringTable asStringTable(Options.DivisorFormat divisorFormat, Options.TieFormat tieFormat)
		{
			StringTable st = new StringTable();
			st.append(quotientColumn(divisorFormat));
			st.append(resultColumn(divisorFormat, tieFormat));
			return st;
		}

		public default StringTable resultColumn(Options.DivisorFormat divisorFormat, Options.TieFormat tieFormat)
		{
			StringTable st = new StringTable();
			StringTable.Column col = st.col(0);

			col.append(name());

			for (MonopropOutput.Party party : output().parties())
			{
				String s = party.seats().toString();
				if (party.uniqueness() == Uniqueness.CAN_BE_MORE)
					s += Resources.get("output.can_be_more");
				else if (party.uniqueness() == Uniqueness.CAN_BE_LESS)
					s += Resources.get("output.can_be_more");

				col.append(s);
			}

			Int sum = output().parties().stream().map(MonopropOutput.Party::seats).reduce(Int::add).get();
			col.append(sum);
			if (divisorFormat != Options.DivisorFormat.QUOTIENT)
				col.append(divquo(divisorFormat));

			return st;
		}
	}


	public static class Divisor implements BasicMethod
	{
		private final DivisorAlgorithm method;

		public Divisor(DivisorAlgorithm method)
		{
			this.method = method;

		}

		public Output calculate(MonopropInput input)
		{
			return new Output(method.calculateDeep(input));
		}

		public class Output implements BasicMethod.Output
		{
			private final DivisorOutput output;

			public Output(DivisorOutput output)
			{
				this.output = output;
			}

			@Override
			public MonopropOutput output()
			{
				return output;
			}

			@Override
			public BasicMethod method()
			{
				return Divisor.this;
			}

			public MonopropOutput.Party findParty(MonopropInput.Party party)
			{
				return output.parties().find(party.name());
			}

			public Output outputFor(MonopropInput.Party party)
			{
				return new Output((DivisorOutput) findParty(party).apparentment());
			}

			@Override
			public StringTable quotientColumn(Options.DivisorFormat divisorFormat)
			{
				StringTable st = new StringTable();
				StringTable.Column col = st.col(0);

				col.append(Resources.get("output.quotient"));

				for (MonopropOutput.Party party : output.parties())
					col.append(RoundingHelper.round(party.votes().div(output.divisor().nice()), 3, 100, method.roundingFunction()));

				if (divisorFormat == Options.DivisorFormat.QUOTIENT)
					col.append(String.format("(%s)", divquo(divisorFormat)));

				return st;
			}
			@Override
			public String divquo(Options.DivisorFormat divisorFormat)
			{
				switch (divisorFormat)
				{
					case DIV_INTERVAL:
						return String.format("[%s..%s]", output.divisor().min(), output.divisor().max());
					case MULT:
						return output.divisor().nice().inv().toString();
					case MULT_INTERVAL:
						return String.format("[%s..%s]", output.divisor().max().inv(), output.divisor().min().inv());
					case DIV_QUO:
					case QUOTIENT:
					default:
						return output.divisor().nice().toString();
				}
			}

			@Override
			public String name()
			{
				if (method.roundingFunction() instanceof RoundingFunction.Stationary)
				{
					Rational r = method.roundingFunction().getParam();
					if (r.equals(BMath.ZERO)) return Resources.get("method.divupw");
					if (r.equals(BMath.HALF)) return Resources.get("method.divstd");
					if (r.equals(BMath.ONE)) return Resources.get("method.divdwn");
					return Resources.get("method.divsta", r);
				}
				if (method.roundingFunction() instanceof RoundingFunction.Power)
				{
					Rational p = method.roundingFunction().getParam();
					if (p.equals(BMath.ZERO)) return Resources.get("method.divgeo");
					if (p.equals(BMath.MINUS_ONE)) return Resources.get("method.divhar");
					return Resources.get("method.divpow", p);
				}
				return method.getClass().toString();
			}
		}
	}

	public static class Quota
	{
		public static class Output
		{
			private final QuotaOutput output;

			public Output(QuotaOutput output)
			{
				this.output = output;
			}
		}
	}
}
