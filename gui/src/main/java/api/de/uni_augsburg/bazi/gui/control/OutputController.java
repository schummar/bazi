package de.uni_augsburg.bazi.gui.control;

import de.uni_augsburg.bazi.common.plain.DivisorFormat;
import de.uni_augsburg.bazi.common.plain.Orientation;
import de.uni_augsburg.bazi.common.plain.TieFormat;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;

/**
 * Created by Marco on 06.07.2014.
 */
public class OutputController
{
	private final ComboBox<Orientation> orientation;
	private final ComboBox<DivisorFormat> divisorFormat;
	private final ComboBox<TieFormat> tieFormat;


	public OutputController(ComboBox<Orientation> orientation, ComboBox<DivisorFormat> divisorFormat, ComboBox<TieFormat> tieFormat)
	{
		this.orientation = orientation;
		this.divisorFormat = divisorFormat;
		this.tieFormat = tieFormat;

		orientation.setItems(
			FXCollections.observableArrayList(
				Orientation.HORIZONTAL,
				Orientation.VERTICAL,
				Orientation.HORVER,
				Orientation.VERHOR
			)
		);
		orientation.setValue(Orientation.HORIZONTAL);

		divisorFormat.setItems(
			FXCollections.observableArrayList(
				DivisorFormat.DIV_SPLIT,
				DivisorFormat.INTERVAL,
				DivisorFormat.MULT,
				DivisorFormat.MULT_INTERVAL,
				DivisorFormat.QUOTIENTS
			)
		);
		divisorFormat.setValue(DivisorFormat.DIV_SPLIT);

		tieFormat.setItems(
			FXCollections.observableArrayList(
				TieFormat.CODED,
				TieFormat.CODED_LIST,
				TieFormat.LIST
			)
		);
		tieFormat.setValue(TieFormat.CODED);
	}
}
