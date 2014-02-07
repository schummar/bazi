import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Rational;
import de.uni_augsburg.bazi.monoprop.MonopropInput;

public class BipropOutput
{
	public static class Party implements MonopropInput.Party
	{
		Object id;
		String name;
		Rational votes;
		Int min, max, dir, seats;

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

		@Override
		public Object id() { return id; }
		@Override
		public String name() { return name; }
		@Override
		public Rational votes() { return votes; }
		@Override
		public Int min() { return min; }
		@Override
		public Int max() { return max; }
		@Override
		public Int dir() { return dir; }
		public Int seats() { return seats; }
	}
}
