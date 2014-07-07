package de.uni_augsburg.bazi.gui.control;

import de.uni_augsburg.bazi.common.plain.DivisorFormat;
import de.uni_augsburg.bazi.common.plain.Orientation;
import de.uni_augsburg.bazi.common.plain.PlainOptions;
import de.uni_augsburg.bazi.common.plain.TieFormat;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

/**
 * Created by Marco on 06.07.2014.
 */
public class OutputController
{
	private final ComboBox<Orientation> orientation;
	private final ComboBox<DivisorFormat> divisorFormat;
	private final ComboBox<TieFormat> tieFormat;
	private final TextArea output;
	private final PlainOptions plainOptions;


	public OutputController(ComboBox<Orientation> orientation, ComboBox<DivisorFormat> divisorFormat, ComboBox<TieFormat> tieFormat, TextArea output, PlainOptions plainOptions)
	{
		this.orientation = orientation;
		this.divisorFormat = divisorFormat;
		this.tieFormat = tieFormat;
		this.output = output;
		this.plainOptions = plainOptions;

		orientation.setItems(
			FXCollections.observableArrayList(
				Orientation.HORIZONTAL,
				Orientation.VERTICAL,
				Orientation.HORVER,
				Orientation.VERHOR
			)
		);
		orientation.valueProperty().bindBidirectional(plainOptions.orientationProperty());

		divisorFormat.setItems(
			FXCollections.observableArrayList(
				DivisorFormat.DIV_SPLIT,
				DivisorFormat.INTERVAL,
				DivisorFormat.MULT,
				DivisorFormat.MULT_INTERVAL,
				DivisorFormat.QUOTIENTS
			)
		);
		divisorFormat.valueProperty().bindBidirectional(plainOptions.divisorFormatProperty());

		tieFormat.setItems(
			FXCollections.observableArrayList(
				TieFormat.CODED,
				TieFormat.CODED_LIST,
				TieFormat.LIST
			)
		);
		tieFormat.valueProperty().bindBidirectional(plainOptions.tieFormatProperty());
	}

	public void append(String s)
	{
		output.appendText(s);
	}
}
