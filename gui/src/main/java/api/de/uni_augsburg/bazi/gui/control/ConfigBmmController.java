package de.uni_augsburg.bazi.gui.control;

import de.uni_augsburg.bazi.common.Resources;
import javafx.fxml.FXML;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextField;


public class ConfigBmmController
{
	@FXML TextField base, min, max;
	private Labeled label;


	@FXML void initialize()
	{
		base.setText("0");
		min.setText("0");
		max.setText("oo");
		base.textProperty().addListener(o -> update());
		min.textProperty().addListener(o -> update());
		max.textProperty().addListener(o -> update());
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
			label.setText(Resources.get("gui.methods.bmm.aux", base.getText(), min.getText(), max.getText()));
		}
	}
}
