package de.uni_augsburg.bazi.gui.control;

import javafx.beans.property.Property;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class ConfigMinxpController
{
	@FXML TextField percent;

	public void bind(Property<String> property)
	{
		percent.textProperty().bindBidirectional(property);
	}
}
