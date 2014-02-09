import de.uni_augsburg.bazi.math.*;
import de.uni_augsburg.bazi.monoprop.*;

import java.util.*;
import java.util.stream.Collectors;

public class ASAlgorithm
{
	public static BipropOutput calculate(
		Matrix<BipropOutput.Party> matrix,
		List<Int> rowSeats,
		List<Int> colSeats,
		DivisorUpdateFunction divisorUpdateFunction,
		DivisorMethod divisorMethod
	) throws InterruptedException
	{
		return new ASAlgorithm(matrix, divisorUpdateFunction, divisorMethod, rowSeats, colSeats).calculate();
	}


	private final Matrix<BipropOutput.Party> matrix;
	private Set<Integer> rows, cols;
	private List<Int> rowSeats, colSeats;
	private List<Real> rowDivisors, colDivisors;
	private final DivisorUpdateFunction divisorUpdateFunction;
	private final DivisorMethod divisorMethod;
	private ASAlgorithm(Matrix<BipropOutput.Party> matrix, DivisorUpdateFunction divisorUpdateFunction, DivisorMethod divisorMethod, List<Int> rowSeats, List<Int> colSeats)
	{
		this.matrix = matrix;
		this.rows = Interval.zeroTo(matrix.rows());
		this.cols = Interval.zeroTo(matrix.cols());
		this.rowDivisors = rows.stream().map(r -> BMath.ONE).collect(Collectors.toList());
		this.colDivisors = cols.stream().map(c -> BMath.ONE).collect(Collectors.toList());
		this.divisorUpdateFunction = divisorUpdateFunction;
		this.divisorMethod = divisorMethod;
		this.rowSeats = rowSeats;
		this.colSeats = colSeats;
	}

	private BipropOutput calculate() throws InterruptedException
	{
		boolean isRowStep = true;
		List<Int> flaws = rows.stream().map(r -> BMath.ZERO).collect(Collectors.toList());
		Int flawCount = BMath.ONE;

		while (!flawCount.equals(0))
		{
			if (Thread.interrupted())
				throw new InterruptedException();

			// scale votes
			for (int row : rows)
				for (int col : cols)
				{
					BipropOutput.Party party = matrix.get(row, col);
					party.votes = party.votes.div(rowDivisors.get(row).mul(colDivisors.get(col)));
				}

			// calculate one apportionment per row (or column)
			rows.parallelStream().forEach(
				row -> {
					DivisorOutput output = divisorMethod.calculate(new Input(rowSeats.get(row), matrix.row(row)));
					for (int col : cols)
					{
						matrix.get(row, col).seats = output.parties().get(col).seats();
						matrix.get(row, col).uniqueness = output.parties().get(col).uniqueness();
					}
					rowDivisors.set(row, divisorUpdateFunction.update(rowDivisors.get(row), output.divisor(), null));
				}
			);

			transfer();

			if (!isRowStep) matrix.transpose();
			System.out.println(matrix.toString(p -> p.seats().toString()+p.uniqueness().toString()));
			if (!isRowStep) matrix.transpose();

			// update flaws
			flaws = cols.stream()
				.map(
					col -> matrix.col(col).stream()
						.map(BipropOutput.Party::seats)
						.reduce(Int::add).get().sub(colSeats.get(col))
				)
				.collect(Collectors.toList());
			System.out.println(flaws);
			flawCount = flaws.stream()
				.map(flaw -> flaw.max(0))
				.reduce(Int::add).get();

			// flip rows / cols
			isRowStep = !isRowStep;
			transpose();
		}

		if (!isRowStep)
			transpose();

		System.out.println(matrix);

		return null;
	}

	private void transfer()
	{
		Set<Integer> Iminus = new HashSet<>(), Iplus = new HashSet<>();

		for (int col : cols)
		{
			Int sum = matrix.col(col).stream()
				.map(BipropOutput.Party::seats)
				.reduce(Int::add)
				.orElse(BMath.ZERO);
			int comp = sum.compareTo(colSeats.get(col));
			if (comp < 0) Iminus.add(col);
			if (comp > 0) Iplus.add(col);
		}

		Set<Integer> L = new HashSet<>(Iminus);
		Queue<Integer> Q = new LinkedList<>(L);
		int[] pre = new int[matrix.rows() + matrix.cols()];
		int colCount = matrix.cols();

		while (!Q.isEmpty())
		{
			int u = Q.poll();
			if (u < colCount)
			{
				int col = u;
				for (int row : rows)
					if (!L.contains(row + colCount)
						&& matrix.get(row, col).votes.compareTo(0) > 0
						&& matrix.get(row, col).uniqueness == Uniqueness.CAN_BE_MORE)
					{
						Q.add(row + colCount);
						L.add(row + colCount);
						pre[row + colCount] = col;
					}
			}
			else
			{
				int row = u - colCount;
				for (int col : cols)
					if (!L.contains(col)
						&& matrix.get(row, col).votes.compareTo(0) > 0
						&& matrix.get(row, col).uniqueness == Uniqueness.CAN_BE_LESS)
					{
						Q.add(col);
						L.add(col);
						pre[col] = row + colCount;
					}
			}
		}

		List<Integer> L_Iplus = new ArrayList<>(L);
		L_Iplus.retainAll(Iplus);

		if (!L_Iplus.isEmpty())
		{
			int col = L_Iplus.get(0);
			do
			{
				int row = pre[col] - colCount;
				BipropOutput.Party p = matrix.get(row, col);
				p.seats = p.seats.sub(1);
				p.uniqueness = Uniqueness.CAN_BE_MORE;

				col = pre[row + colCount];
				p = matrix.get(row, col);
				p.seats = p.seats.add(1);
				p.uniqueness = Uniqueness.CAN_BE_LESS;
			} while (!Iminus.contains(col));
			transfer();
		}
	}


	private void transpose()
	{
		matrix.transpose();
		Set<Integer> tempRows = rows;
		rows = cols;
		cols = tempRows;
		List<Int> tempRowSeats = rowSeats;
		rowSeats = colSeats;
		colSeats = tempRowSeats;
		List<Real> tempRowDivisors = rowDivisors;
		rowDivisors = colDivisors;
		colDivisors = tempRowDivisors;
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


	public static void main(String[] args) throws InterruptedException
	{
		Rational[][] votes = {{BMath.TWO, BMath.ONE}, {BMath.TWO, BMath.ONE}};
		Matrix<BipropOutput.Party> matrix = new Matrix<>(2, 2, (r, c) -> new BipropOutput.Party(votes[r][c]));
		List<Int> rowSeats = Arrays.asList(BMath.valueOf(5), BMath.valueOf(5));
		List<Int> colSeats = Arrays.asList(BMath.valueOf(7), BMath.valueOf(3));
		DivisorMethod method = new DivisorMethod(RoundingFunction.DIV_STD);

		calculate(matrix, rowSeats, colSeats, DivisorUpdateFunction.MIDPOINT, method);
	}
}
