import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Real;
import de.uni_augsburg.bazi.monoprop.MonopropInput;
import de.uni_augsburg.bazi.monoprop.Uniqueness;

public class BipropOutput
{
	public static class Party implements MonopropInput.Party
	{
		Object id;
		String name;
		Real votes;
		Int min, max, dir, seats;
		Uniqueness uniqueness;

		public Party(MonopropInput.Party party)
		{
			this.id = party.id();
			this.name = party.name();
			this.votes = party.votes();
			this.min = party.min();
			this.max = party.max();
			this.dir = party.dir();
			this.seats = party.dir();
		}

		public Party(Real votes)
		{
			this.votes = votes;
			id = this;
			name = "";
			min = dir = BMath.ZERO;
			max = BMath.INF;
		}
		@Override
		public Object id() { return id; }
		@Override
		public String name() { return name; }
		@Override
		public Real votes() { return votes; }
		@Override
		public Int min() { return min; }
		@Override
		public Int max() { return max; }
		@Override
		public Int dir() { return dir; }
		public Int seats() { return seats; }
		public Uniqueness uniqueness() { return uniqueness; }
	}
}
