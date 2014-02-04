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

	public Output calculate(MonopropInput input);

	public static StringTable asStringTable(List<? extends MonopropInput.Party> parties, List<Output> outputs, Options.Orientation orientation, Options.DivisorFormat divisorFormat, Options.TieFormat tieFormat)
	{
		StringTable st = new StringTable();
		StringTable.Column names = st.col(0), votes = st.col(1), conditions = st.col(2);

		names.append(Resources.get("output.names"));
		votes.append(Resources.get("output.votes"));
		conditions.append(Resources.get("output.conditions"));

		for (MonopropInput.Party party : parties)
		{
			names.append(party.name());
			votes.append(party.votes());
			conditions.append(String.format("%s|%s..%s", party.dir(), party.min(), party.max()));
		}

		boolean divisor = outputs.stream().map(Output::method).anyMatch(Divisor.class::isInstance);
		boolean quota = outputs.stream().map(Output::method).anyMatch(Quota.class::isInstance);
		String label = divisor && quota ? "div_quo" : (divisor ? "div" : "quo");
		if (divisorFormat == Options.DivisorFormat.QUOTIENT)
		{
			names.append(String.format("%s (%s)", Resources.get("output.sum"), Resources.get("output.div_quo." + label)));
		}
		else
		{
			names.append(Resources.get("output.sum"));
			names.append(Resources.get("output." + divisorFormat.name().toLowerCase() + "." + label));
		}

		Rational sum = parties.stream().map(MonopropInput.Party::votes).reduce(Rational::add).get();
		Rational dirSum = parties.stream().map(MonopropInput.Party::dir).reduce(Int::add).get();
		Rational minSum = parties.stream().map(MonopropInput.Party::min).reduce(Int::add).get();
		Rational maxSum = parties.stream().map(MonopropInput.Party::max).reduce(Int::add).get();
		votes.append(sum);
		conditions.append(String.format("%s|%s..%s", dirSum, minSum, maxSum));

		for (Output output : outputs)
			st.append(output.asStringTable(divisorFormat, tieFormat));

		return orientation == Options.Orientation.VERTICAL ? st : st.transposed();
	}


	public interface Output
	{
		public Method method();
		public MonopropOutput output();

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

			if (divisorFormat != Options.DivisorFormat.QUOTIENT)
				col.append(divquo(divisorFormat));

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
			public MonopropOutput output()
			{
				return output;
			}

			@Override
			public Method method()
			{
				return Divisor.this;
			}

			@Override
			public StringTable quotientColumn(Options.DivisorFormat divisorFormat)
			{
				StringTable st = new StringTable();
				StringTable.Column col = st.col(0);

				col.append(Resources.get("output.quotient"));

				for (MonopropOutput.Party party : output.parties())
					col.append(RoundingHelper.round(party.votes().div(output.divisor().nice()), 3, 100, method.roundingFunction()));

				col.append(divquo(divisorFormat));

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

	public class Quota
	{}
}
