package de.uni_augsburg.bazi.cl;

import de.uni_augsburg.bazi.common.Resources;
import de.uni_augsburg.bazi.common.Tuple;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Rational;
import de.uni_augsburg.bazi.monoprop.MonopropInput;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class BasicMethods
{
	public static OutputPackage calculate(List<BasicMethod> methods, List<Int> seats, MonopropInput input)
	{
		return null;
	}

	public static class OutputPackage
	{
		private final SortedMap<Tuple<BasicMethod, Int>, BasicMethod.Output> outputs = new TreeMap<>();


		public StringTable asStringTable(List<? extends MonopropInput.Party> parties, List<BasicMethod.Output> outputs, Options.Orientation orientation, Options.DivisorFormat divisorFormat, Options.TieFormat tieFormat)
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
