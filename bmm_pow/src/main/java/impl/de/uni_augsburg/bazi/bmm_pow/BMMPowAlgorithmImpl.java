package de.uni_augsburg.bazi.bmm_pow;

import de.uni_augsburg.bazi.common.algorithm.Options;
import de.uni_augsburg.bazi.common.algorithm.VectorInput;
import de.uni_augsburg.bazi.common.algorithm.VectorOutput;
import de.uni_augsburg.bazi.common.data.Data;
import de.uni_augsburg.bazi.common.util.Tuple;
import de.uni_augsburg.bazi.divisor.DivisorAlgorithm;
import de.uni_augsburg.bazi.divisor.DivisorOutput;
import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Interval;
import de.uni_augsburg.bazi.math.Real;

import java.util.ArrayList;
import java.util.List;

import static de.uni_augsburg.bazi.bmm_pow.BMMPowOutput.BMMPowResult;
import static de.uni_augsburg.bazi.common.algorithm.VectorOutput.Party;

/**
 * Created by Marco on 12.03.14.
 */
class BMMPowAlgorithmImpl
{
	public static BMMPowOutput calculate(VectorInput in, DivisorAlgorithm method, Int base, Int min, Int max, Options options)
	{
		return new BMMPowAlgorithmImpl(in, method, base, min, max, options).calculate();
	}


	private VectorInput in;
	private DivisorOutput dout;
	private final DivisorAlgorithm method;
	private final Int base, min, max;
	private final Options options;
	private final Int bound;
	BMMPowAlgorithmImpl(VectorInput in, DivisorAlgorithm method, Int base, Int min, Int max, Options options)
	{
		this.in = in;
		this.method = method;
		this.base = base;
		this.min = min;
		this.max = max;
		this.options = options;
		this.bound = min.add(1).max(method.roundingFunction().isImpervious() ? BMath.TWO : BMath.ONE);
	}

	public BMMPowOutput calculate()
	{
		dout = method.apply(dout, options);

		List<Real> powers = new ArrayList<>();
		Party strongest = strongest(dout.parties());

		if (strongest.seats().compareTo(max) <= 0)
			powers.add(BMath.ONE);
		else
		{
			Real Emin = BMath.ONE;
			while (strongest.seats().compareTo(max) > 0)
				Emin = transfer();
			while (strongest.seats().equals(max))
			{
				Real Emax = Emin;
				Emin = transfer();
				powers.add(Interval.of(Emin, Emax).nice());
			}
			powers.sort(Real::compareTo);
		}

		BMMPowOutput out = Data.create(BMMPowOutput.class);
		powers.forEach(
			pow -> {
				VectorOutput partInt = in.copy().cast(VectorOutput.class);
				partInt.parties().forEach(
					p -> {
						p.min(min);
						p.max(BMath.INF);
						p.votes(BMath.pow(p.votes(), pow, options.precision()));
					}
				);

				BMMPowResult partOut = method.apply(partInt, options).cast(BMMPowResult.class);
				partOut.power(pow);
				out.results().add(partOut);
			}
		);

		return out;
	}


	private static Party strongest(List<? extends Party> parties)
	{
		Party strongest = parties.get(0);
		for (Party party : parties)
			if (party.seats().compareTo(strongest.seats()) > 0) strongest = party;
		return strongest;
	}

	private Real E(Party a, Party b)
	{
		Real temp = method.roundingFunction().getBorder(b.seats().sub(1), options.precision());
		temp = temp.div(method.roundingFunction().getBorder(a.seats(), options.precision()));
		return temp.div(BMath.log(b.votes().div(a.votes()), options.precision()));
	}

	private Real transfer()
	{
		Real E = BMath.INFN;
		Tuple<Party, Party> transfer = null;

		for (Party a : dout.parties())
			for (Party b : dout.parties())
			{
				if (!(a.votes().compareTo(b.votes()) > 0 && a.seats().compareTo(bound) >= 0))
					continue;
				Real temp = E(b, a);
				if (temp.compareTo(E) > 0)
				{
					E = temp;
					transfer = Tuple.of(a, b);
				}
			}

		transfer.x().seats(transfer.x().seats().sub(1));
		transfer.y().seats(transfer.y().seats().add(1));
		return E;
	}
}
