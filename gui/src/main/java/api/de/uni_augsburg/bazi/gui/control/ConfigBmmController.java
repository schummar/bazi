package de.uni_augsburg.bazi.gui.control;

import javafx.beans.property.Property;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;


public class ConfigBmmController
{
	@FXML TextField base, min, max;

	public void bind(Property<String> base, Property<String> min, Property<String> max)
	{
		this.base.textProperty().bindBidirectional(base);
		this.min.textProperty().bindBidirectional(min);
		this.max.textProperty().bindBidirectional(max);
	}
}
