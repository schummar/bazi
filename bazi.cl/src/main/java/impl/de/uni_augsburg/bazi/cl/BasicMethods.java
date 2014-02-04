package de.uni_augsburg.bazi.cl;

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
		OutputPackage op = new OutputPackage();
		for (BasicMethod method : methods)
			for (Int seat : seats)
				op.outputs.put(Tuple.of(method, seat), method.calculate(new Input(seat, parties)));

		return op;
	}

	public static class OutputPackage
	{
		private final List<MonopropInput.Party> parties;
		private final SortedMap<Tuple<BasicMethod, Int>, BasicMethod.Output> outputs = new TreeMap<>();

		public OutputPackage(List<MonopropInput.Party> parties)
		{
			this.parties = parties;
		}

		public BasicMethod.Output get(BasicMethod method, Int seats) { return outputs.get(Tuple.of(method, seats)); }

		public List<StringTable> asStringTables(Options.Orientation orientation, Options.DivisorFormat divisorFormat, Options.TieFormat tieFormat)
		{
			List<StringTable> stringTables = new ArrayList<>();
			stringTables.add(asStringTable(orientation, divisorFormat, tieFormat));

			Queue<Tuple<MonopropInput.Party, Collection<BasicMethod.Output>>> q = new LinkedList<>();
			q.addAll(parties.stream()
				.filter(p -> !p.parties().isEmpty())
				.map(p -> Tuple.of(p, outputs.values()))
				.collect(Collectors.toList()));

			while (!q.isEmpty())
			{
				Tuple<MonopropInput.Party, Collection<BasicMethod.Output>> current = q.remove();

				Queue<Tuple<MonopropInput.Party, Collection<BasicMethod.Output>>> newQ = new LinkedList<>();
				newQ.addAll(parties.stream()
					.filter(p -> !p.parties().isEmpty())
					.map(p -> Tuple.of(p, outputs.values()))
					.collect(Collectors.toList()));
				List<BasicMethod.Output> newOutputs = current.y().stream().map(o->o.get(current.x().parties()))
			}
		}

		public StringTable asStringTable(Options.Orientation orientation, Options.DivisorFormat divisorFormat, Options.TieFormat tieFormat)
		{
			return asStringTable(outputs.values(), orientation, divisorFormat, tieFormat);
		}

		private StringTable asStringTable(Collection<BasicMethod.Output> outputs, Options.Orientation orientation, Options.DivisorFormat divisorFormat, Options.TieFormat tieFormat)
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
