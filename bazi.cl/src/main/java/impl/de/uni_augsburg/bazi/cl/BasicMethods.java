package de.uni_augsburg.bazi.cl;

import com.google.common.collect.Lists;
import de.uni_augsburg.bazi.common.Json;
import de.uni_augsburg.bazi.common.Resources;
import de.uni_augsburg.bazi.common.Tuple;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Rational;
import de.uni_augsburg.bazi.math.Real;
import de.uni_augsburg.bazi.monoprop.MonopropInput;

import java.util.*;
import java.util.stream.Collectors;

public class BasicMethods
{
	public static OutputPackage calculate(List<BasicMethod> methods, List<Int> seats, List<? extends MonopropInput.Party> parties, Options options)
	{
		OutputPackage op = new OutputPackage(parties, options);
		for (BasicMethod method : methods)
			for (Int seat : seats)
				op.outputs.put(Tuple.of(method, seat), method.calculate(new Input(seat, parties)));

		return op;
	}

	public static class OutputPackage implements Format.Formatter
	{
		private final List<? extends MonopropInput.Party> parties;
		private final Options options;
		private final Map<Tuple<BasicMethod, Int>, BasicMethod.Output> outputs = new LinkedHashMap<>();

		private OutputPackage(List<? extends MonopropInput.Party> parties, Options options)
		{
			this.parties = parties;
			this.options = options;
		}

		public List<BasicMethod.Output> getAll() { return new ArrayList<>(outputs.values()); }
		public BasicMethod.Output get(BasicMethod method, Int seats) { return outputs.get(Tuple.of(method, seats)); }


		@Override
		public String plain()
		{
			return asStringTables().stream()
				.map(StringTable::toString)
				.collect(Collectors.joining());
		}

		@Override
		public String json()
		{
			return Json.toJson(
				getAll().stream()
					.map(BasicMethod.Output::output)
					.collect(Collectors.toList())
			);
		}


		public List<StringTable> asStringTables()
		{
			List<StringTable> stringTables = new ArrayList<>();

			Deque<Tuple<List<? extends MonopropInput.Party>, List<? extends BasicMethod.Output>>> q = new LinkedList<>();
			q.add(Tuple.of(parties, new ArrayList<>(outputs.values())));

			while (!q.isEmpty())
			{
				List<? extends MonopropInput.Party> parties = q.peekFirst().x();
				List<? extends BasicMethod.Output> outputs = q.removeFirst().y();
				stringTables.add(asStringTable(parties, outputs));

				Lists.reverse(parties).stream()
					.filter(p -> !p.parties().isEmpty())
					.forEach(
						p -> q.addFirst(
							Tuple.of(
								p.parties(),
								outputs.stream().map(o -> o.outputFor(p)).collect(Collectors.toList())
							)
						)
					);
			}
			return stringTables;
		}

		public StringTable asStringTable()
		{
			return asStringTable(parties, outputs.values());
		}

		private StringTable asStringTable(List<? extends MonopropInput.Party> parties, Collection<? extends BasicMethod.Output> outputs)
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

			boolean divisor = outputs.stream().map(BasicMethod.Output::method).anyMatch(BasicMethod.Divisor.class::isInstance);
			boolean quota = outputs.stream().map(BasicMethod.Output::method).anyMatch(BasicMethod.Quota.class::isInstance);
			String label = divisor && quota ? "div_quo" : (divisor ? "div" : "quo");
			if (options.divisorFormat == Options.DivisorFormat.QUOTIENT)
			{
				names.append(String.format("%s (%s)", Resources.get("output.sum"), Resources.get("output.div_quo." + label)));
			}
			else
			{
				names.append(Resources.get("output.sum"));
				names.append(Resources.get("output." + options.divisorFormat.name().toLowerCase() + "." + label));
			}

			Real sum = parties.stream().map(MonopropInput.Party::votes).reduce(Real::add).get();
			Rational dirSum = parties.stream().map(MonopropInput.Party::dir).reduce(Int::add).get();
			Rational minSum = parties.stream().map(MonopropInput.Party::min).reduce(Int::add).get();
			Rational maxSum = parties.stream().map(MonopropInput.Party::max).reduce(Int::add).get();
			votes.append(sum);
			conditions.append(String.format("%s|%s..%s", dirSum, minSum, maxSum));

			for (BasicMethod.Output output : outputs)
				st.append(output.asStringTable(options.divisorFormat, options.tieFormat));

			return options.orientation == Options.Orientation.VERTICAL ? st : st.transposed();
		}
	}
}
