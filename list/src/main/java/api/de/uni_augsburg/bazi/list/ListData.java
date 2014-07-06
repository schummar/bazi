package de.uni_augsburg.bazi.list;

import de.schummar.castable.Attribute;
import de.schummar.castable.DataList;
import de.uni_augsburg.bazi.common.algorithm.VectorData;
import de.uni_augsburg.bazi.math.Int;

/** Output of the ListAlgorithm. */
public interface ListData extends VectorData
{
	@Override @Attribute DataList<? extends Party> parties();

	public interface Party extends VectorData.Party, VectorData
	{
		@Override default String name() { return nameProperty().getValue(); }
		@Override default void name(String v) { nameProperty().setValue(v); }
		@Override default Int seats() { return seatsProperty().getValue(); }
		@Override default void seats(Int v) { seatsProperty().setValue(v); }

		/**
		 * The parties in this combined list.
		 * @return the parties in this combined list.
		 */
		@Attribute DataList<ListData.Party> parties();
	}
}
