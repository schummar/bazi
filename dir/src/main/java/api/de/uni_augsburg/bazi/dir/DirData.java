package de.uni_augsburg.bazi.dir;

import de.schummar.castable.Attribute;
import de.schummar.castable.DataList;
import de.uni_augsburg.bazi.list.ListData;
import de.uni_augsburg.bazi.math.Int;
import javafx.beans.property.Property;

/** Output of the DirFilter */
public interface DirData extends ListData
{
	@Override @Attribute DataList<? extends Party> parties();


	public interface Party extends ListData.Party
	{
		/**
		 * The number of direct seats the party gets.
		 * @return the number of direct seats the party gets.
		 */
		@Attribute Property<Int> dirProperty();
		default Int dir() { return dirProperty().getValue(); }
		default void dir(Int v) { dirProperty().setValue(v); }
	}
}
