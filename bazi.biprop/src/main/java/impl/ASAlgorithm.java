import com.google.common.collect.Sets;
import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Interval;
import de.uni_augsburg.bazi.math.Real;
import de.uni_augsburg.bazi.monoprop.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public class ASAlgorithm
{
	public static Object calculate(
		Matrix<BipropOutput.Party> matrix,
		List<Int> rowSeats,
		List<Int> colSeats,
		DivisorUpdateFunction divisorUpdateFunction,
		DivisorMethod method
	) throws InterruptedException
	{
		Set<Integer> rows = Interval.zeroTo(matrix.rows());
		Set<Integer> cols = Interval.zeroTo(matrix.cols());

		boolean isRowStep = true;
		List<Real> rowDivisors = new ArrayList<>();
		rows.forEach(r -> rowDivisors.add(BMath.ONE));
		List<Real> colDivisors = new ArrayList<>();
		cols.forEach(c -> colDivisors.add(BMath.ONE));

		Supplier<Set<Integer>> curRows = () -> isRowStep?rows:cols;

		List<Int> seats = rowSeats;
		List<Real> divisors = rowDivisors;

		while (true)
		{
			if (Thread.interrupted())
				throw new InterruptedException();

			// current flaws


			// scale votes
			for (int row : rows)
				for (int col : cols)
				{
					BipropOutput.Party party = matrix.get(row, col);
					party.votes = party.votes.div(rowDivisors.get(row).mul(colDivisors.get(col)));
				}

			// calculate one apportionment per row (or column)
			for (int row : rows)
			{
				DivisorOutput output = method.calculate(new Input(rowSeats.get(row), matrix.row(row)));
				for (int col : cols)
					matrix.get(row, col).seats = output.parties().get(col).seats();
				divisors.set(row, divisorUpdateFunction.update(divisors.get(row), output.divisor(), null));
			}

			// flip rows / cols
			isRowStep = !isRowStep;
			matrix.transpose();
			seats = isRowStep ? rowSeats : colSeats;
			divisors = isRowStep ? rowDivisors : colDivisors;
		}


		return null;
	}

	private static class Input implements MonopropInput
	{
		private final Int seats;
		private final List<? extends Party> parties;
		private Input(Int seats, List<? extends Party> parties)
		{
			this.seats = seats;
			this.parties = parties;
		}
		@Override
		public Int seats() { return seats; }
		@Override
		public List<? extends Party> parties() { return parties; }
	}
}
