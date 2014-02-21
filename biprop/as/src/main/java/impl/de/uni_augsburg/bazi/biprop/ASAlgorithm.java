package de.uni_augsburg.bazi.biprop;

class ASAlgorithm
{
	/*public static Map<Object, Real> calculate(
		Table<BipropInput.District, String, BipropOutput.Party> table,
		Map<Object, Int> seats,
		DivisorUpdateFunction divisorUpdateFunction,
		DivisorAlgorithm divisorAlgorithm
	)
	{
		return new ASAlgorithm(divisorUpdateFunction, divisorAlgorithm, table, seats).calculate();
	}


	Table<BipropInput.District, String, BipropOutput.Party> table;
	Map<?, ? extends Map<?, BipropOutput.Party>> rows, cols, rowsAndCols;
	Map<Object, Int> seats;
	private final DivisorUpdateFunction divisorUpdateFunction;
	private final DivisorAlgorithm divisorAlgorithm;

	ASAlgorithm(DivisorUpdateFunction divisorUpdateFunction, DivisorAlgorithm divisorAlgorithm, Table<BipropInput.District, String, BipropOutput.Party> table, Map<Object, Int> seats)
	{
		this.divisorUpdateFunction = divisorUpdateFunction;
		this.divisorAlgorithm = divisorAlgorithm;
		this.table = table;
		this.seats = seats;
		rows = table.rowMap();
		cols = table.columnMap();

		Map<Object, Map<?, BipropOutput.Party>> rowsAndCols = new HashMap<>();
		rowsAndCols.putAll(rows);
		rowsAndCols.putAll(cols);
		this.rowsAndCols = rowsAndCols;
	}


	private Map<Object, Real> calculate()
	{
		Map<BipropOutput.Party, Real> votes = table.values().stream().collect(
			Collectors.toMap(Function.identity(), BipropOutput.Party::votes)
		);
		Set<Object> allKeys = Sets.union(table.rowKeySet(), table.columnKeySet());
		Map<Object, Real> divisors = allKeys.stream()
			.collect(Collectors.toMap(Function.identity(), key -> BMath.ONE));

		while (true)
		{
			if (Thread.interrupted())
				throw new UserCanceledException();

			// update faults
			Map<Object, Int> faults = rows.entrySet().stream().collect(
				Collectors.toMap(
					row -> row.getKey(),
					row -> row.getValue().values().stream()
						.map(BipropOutput.Party::seats)
						.reduce(Int::add).orElse(BMath.ZERO)
						.sub(seats.get(row.getKey()))
				)
			);
			if (!faults.values().stream().allMatch(BMath.ZERO::equals))
				break;

			// for each row (or column when transposed)
			rows.entrySet().parallelStream().forEach(
				row -> {

					// scale each votes with the respective column divisor
					row.getValue().forEach(
						(colKey, party) -> party.votes = party.votes.div(divisors.get(colKey))
					);

					// calculate an apportionment => the row sum will be correct
					DivisorOutput output = divisorAlgorithm.calculate(
						new MonopropInput()
						{
							@Override
							public Int seats() { return seats.get(row.getKey()); }
							@Override
							public Collection<? extends Party> parties() { return row.getValue().values(); }
						}
					);

					// apply the calculated seats to the table and reset votes
					row.getValue().values().forEach(
						party -> {
							party.votes = votes.get(party);
							party.seats = output.parties().find(party.name).seats();
							party.uniqueness = output.parties().find(party.name).uniqueness();
						}
					);

					// set the calculated divisor as row divisor
					divisors.compute(
						row.getKey(),
						(key, value) -> divisorUpdateFunction.apply(output.divisor(), faults.get(key))
					);
				}
			);

			// transer ties to minimize faults
			transfer();

			// flip rows / cols
			transpose();
		}

		return divisors;
	}


	private void transfer()
	{
		while (true)
		{
			Set<Object> Iminus = new HashSet<>(), Iplus = new HashSet<>();

			cols.forEach(
				(colKey, col) -> {
					Int sum = col.values().stream()
						.map(BipropOutput.Party::seats)
						.reduce(Int::add).orElse(BMath.ZERO);
					int comp = sum.compareTo(seats.get(colKey));
					if (comp < 0) Iminus.add(colKey);
					if (comp > 0) Iplus.add(colKey);
				}
			);

			Set<Object> L = new HashSet<>(Iminus);
			Queue<Object> Q = new LinkedList<>(L);
			Map<Object, Object> pre = new HashMap<>();

			while (!Q.isEmpty())
			{
				Object u = Q.poll();
				Uniqueness uniqueness = cols.containsKey(u)
					? Uniqueness.CAN_BE_MORE
					: Uniqueness.CAN_BE_LESS;

				rowsAndCols.get(u).forEach(
					(key, party) -> {
						if (!L.contains(key)
							&& party.votes.compareTo(0) > 0
							&& party.uniqueness == uniqueness)
						{
							Q.add(key);
							L.add(key);
							pre.put(key, u);
						}
					}
				);
			}

			List<Object> L_Iplus = new ArrayList<>(L);
			L_Iplus.retainAll(Iplus);

			if (L_Iplus.isEmpty()) return;

			Object colKey = L_Iplus.get(0);
			do
			{
				Object rowKey = pre.get(colKey);
				BipropOutput.Party party = cols.get(colKey).get(rowKey);
				party.seats = party.seats.sub(1);
				party.uniqueness = Uniqueness.CAN_BE_MORE;

				colKey = pre.get(rowKey);
				party = cols.get(colKey).get(rowKey);
				party.seats = party.seats.add(1);
				party.uniqueness = Uniqueness.CAN_BE_LESS;
			} while (!Iminus.contains(colKey));
		}
	}


	private void transpose()
	{
		Map<?, ? extends Map<?, BipropOutput.Party>> temp = rows;
		rows = cols;
		cols = temp;
	}*/
}
