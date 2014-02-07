import de.uni_augsburg.bazi.monoprop.MonopropInput;

import java.util.List;

public interface BipropInput
{
	public List<? extends District> districts();

	public interface District extends MonopropInput
	{
		public String name();
	}
}
