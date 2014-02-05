package de.uni_augsburg.bazi.cl;

import com.google.common.collect.Lists;
import de.uni_augsburg.bazi.common.Resources;
import de.uni_augsburg.bazi.common.Tuple;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Rational;
import de.uni_augsburg.bazi.monoprop.MonopropInput;

import java.util.*;
import java.util.stream.Collectors;

public class BasicMethods
{
	public static OutputPackage calculate(List<BasicMethod> methods, List<Int> seats, List<? extends MonopropInput.Party> parties)
	{
		OutputPackage op = new OutputPackage(parties);
		for (BasicMethod method : methods)
			for (Int seat : seats)
				op.outputs.put(Tuple.of(method, seat), method.calculate(new Input(seat, parties)));

		return op;
	}

	public static class OutputPackage
	{
		private final List<? extends MonopropInput.Party> parties;
		private final Map<Tuple<BasicMethod, Int>, BasicMethod.Output> outputs = new LinkedHashMap<>();

		public OutputPackage(List<? extends MonopropInput.Party> parties)
		{
			this.parties = parties;
		}

		public BasicMethod.Output get(BasicMethod method, Int seats) { return outputs.get(Tuple.of(method, seats)); }

		public List<StringTable> asStringTables(Options.Orientation orientation, Options.DivisorFormat divisorFormat, Options.TieFormat tieFormat)
		{
			List<StringTable> stringTables = new ArrayList<>();

			Deque<Tuple<List<? extends MonopropInput.Party>, List<? extends BasicMethod.Output>>> q = new LinkedList<>();
			q.add(Tuple.of(parties, new ArrayList<>(outputs.values())));

			while (!q.isEmpty())
			{
				List<? extends MonopropInput.Party> parties = q.peekFirst().x();
				List<? extends BasicMethod.Output> outputs = q.removeFirst().y();
				stringTables.add(asStringTable(parties, outputs, orientation, divisorFormat, tieFormat));

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

		public StringTable asStringTable(Options.Orientation orientation, Options.DivisorFormat divisorFormat, Options.TieFormat tieFormat)
		{
			return asStringTable(parties, outputs.values(), orientation, divisorFormat, tieFormat);
		}

		private StringTable asStringTable(List<? extends MonopropInput.Party> parties, Collection<? extends BasicMethod.Output> outputs, Options.Orientation orientation, Options.DivisorFormat divisorFormat, Options.TieFormat tieFormat)
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

			for (BasicMethod.Output output : outputs)
				st.append(output.asStringTable(divisorFormat, tieFormat));

			return orientation == Options.Orientation.VERTICAL ? st : st.transposed();
		}
	}
}
