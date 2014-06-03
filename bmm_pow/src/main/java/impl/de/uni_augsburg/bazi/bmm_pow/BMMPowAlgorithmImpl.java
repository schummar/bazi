package de.uni_augsburg.bazi.bmm_pow;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import de.uni_augsburg.bazi.bmm.BMMAlgorithm;
import de.uni_augsburg.bazi.common.algorithm.Options;
import de.uni_augsburg.bazi.divisor.DivisorAlgorithm;
import de.uni_augsburg.bazi.divisor.DivisorData;
import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Interval;
import de.uni_augsburg.bazi.math.Real;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static de.uni_augsburg.bazi.bmm_pow.BMMPowData.BMMPowResult;
import static de.uni_augsburg.bazi.common.algorithm.VectorData.Party;

class BMMPowAlgorithmImpl
{
	public static void calculate(BMMPowData data, DivisorAlgorithm method, Int base, Int min, Int max, Options options)
	{
		new BMMPowAlgorithmImpl(data, method, base, min, max, options).calculate();
	}


	private BMMPowData data;
	private DivisorData scalingData;
	private final DivisorAlgorithm method;
	private final Int base, min, max;
	private final Options options;
	private final Int bound;
	BMMPowAlgorithmImpl(BMMPowData data, DivisorAlgorithm method, Int base, Int min, Int max, Options options)
	{
		this.data = data;
		this.method = method;
		this.base = base;
		this.min = min;
		this.max = max;
		this.options = options;
		this.bound = min.add(1).max(method.roundingFunction().isImpervious() ? BMath.TWO : BMath.ONE);
	}

	public void calculate()
	{
		scalingData = data.copy().cast(DivisorData.class);
		scalingData.parties().forEach(
			p -> {
				p.min(min);
				p.max(BMath.INF);
			}
		);
		scalingData.seats(scalingData.seats().sub(base.mul(scalingData.parties().size())));
		method.apply(scalingData, options);

		List<Real> powers = new ArrayList<>();
		Party strongest = strongest(scalingData.parties());

		if (strongest.seats().compareTo(max) <= 0)
			powers.add(BMath.ONE);
		else
		{
			Real Emin = BMath.ONE;
			while (strongest.seats().compareTo(max) > 0)
			{
				Emin = transfer();
			}
			while (strongest.seats().equals(max))
			{
				Real Emax = Emin;
				Emin = transfer();
				powers.add(Interval.of(Emin, Emax).nice());
			}
			powers.sort(Real::compareTo);
		}

		powers.parallelStream().map(
			pow -> {
				BMMPowResult part = data.copy().cast(BMMPowResult.class);
				part.parties().forEach(
					p -> p.votes(BMath.pow(p.votes(), pow, options.precision()))
				);

				BMMAlgorithm bmm = new BMMAlgorithm(base, min, BMath.INF, method);
				bmm.apply(part, options);
				part.power(pow);
				return part;
			}
		)
			.forEachOrdered(data.results()::add);
		//data.plain(new BMMPowPlain(out, algorithm));
	}


	private static Party strongest(List<? extends Party> parties)
	{
		Party strongest = parties.get(0);
		for (Party party : parties)
			if (party.seats().compareTo(strongest.seats()) > 0) strongest = party;
		return strongest;
	}


	private Map<Real, Real> logCache = new HashMap<>();
	private Real log(Real x)
	{
		Real log = logCache.get(x);
		if (log == null) logCache.put(x, log = BMath.log(x, options.precision()));
		return log;
	}

	private final Table<Party, Party, Real> ECache = HashBasedTable.create();
	private Real E(Party from, Party to)
	{
		Real E = ECache.get(from, to);
		if (E != null) return E;

		Real a = log(method.roundingFunction().apply(from.seats().sub(BMath.ONE), options.precision())),
			b = log(method.roundingFunction().apply(to.seats(), options.precision())),
			c = log(from.votes()), d = log(to.votes());

		E = a.sub(b).div(c.sub(d));

		ECache.put(from, to, E);
		return E;
	}

	private class Data
	{
		public Real E = BMath.INFN;
		public Party from, to;
		public synchronized void offer(Real E, Party from, Party to)
		{
			if (E.compareTo(this.E) > 0)
			{
				this.E = E;
				this.from = from;
				this.to = to;
			}
		}
	}
	private Real transfer()
	{
		Data d = new Data();

		scalingData.parties().parallelStream().forEach(
			a -> {
				if (a.seats().compareTo(bound) < 0) return;
				Real E = BMath.INFN;
				Party to = null;
				for (Party b : scalingData.parties())
				{
					if (b.votes().compareTo(a.votes()) >= 0) continue;

					Real temp = E(a, b);
					if (temp.compareTo(E) > 0)
					{
						E = temp;
						to = b;
					}
				}

				d.offer(E, a, to);
			}
		);

		d.from.seats(d.from.seats().sub(1));
		d.to.seats(d.to.seats().add(1));

		ECache.row(d.from).clear();
		ECache.column(d.from).clear();
		ECache.row(d.to).clear();
		ECache.column(d.to).clear();
		return d.E;
	}
}
