package de.uni_augsburg.bazi.gui.control;

import de.uni_augsburg.bazi.common.Resources;
import javafx.fxml.FXML;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextField;

public class ConfigMinxpController
{
	@FXML TextField percent;
	private Labeled label;

	@FXML void initialize()
	{
		percent.setText("10");
		percent.textProperty().addListener(o -> update());
		update();
	}


	public void setLabel(Labeled label)
	{
		this.label = label;
		update();
	}
	private void update()
	{
		if (label != null)
		{
			label.setText(Resources.get("gui.methods.minxp.aux", percent.getText()));
		}
	}
}
