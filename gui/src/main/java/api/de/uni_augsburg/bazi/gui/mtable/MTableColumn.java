package de.uni_augsburg.bazi.gui.mtable;

import javafx.beans.property.StringProperty;
import javafx.scene.control.TableColumn;

public class MTableColumn<T> extends TableColumn<T, String>
{
	public MTableColumn(StringProperty text)
	{
		super(null);

		setGraphic(new MTableHeader(text));
	}
}
