package de.uni_augsburg.bazi.gui.control;

import de.schummar.castable.Data;
import de.uni_augsburg.bazi.bmm.BMMAlgorithm;
import de.uni_augsburg.bazi.bmm_pow.BMMPowAlgorithm;
import de.uni_augsburg.bazi.common.Resources;
import javafx.fxml.FXML;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextField;


public class ConfigBmmpController
{
	@FXML TextField base, min, max;
	BMMPowAlgorithm algorithm = Data.create(BMMPowAlgorithm.class);
	private Labeled label;


	@FXML void initialize()
	{
		base.textProperty().bindBidirectional(algorithm.baseProperty().asStringProperty());
		min.textProperty().bindBidirectional(algorithm.minProperty().asStringProperty());
		max.textProperty().bindBidirectional(algorithm.maxProperty().asStringProperty());

		algorithm.baseProperty().addListener(o -> update());
		algorithm.minProperty().addListener(o -> update());
		algorithm.maxProperty().addListener(o -> update());
	}

	public void init(Labeled label)
	{
		this.label = label;
		update();
	}

	private void update()
	{
		if (label == null) return;
		label.setText(Resources.get("gui.methods.bmm.aux", base.getText(), min.getText(), max.getText()));
	}
}
