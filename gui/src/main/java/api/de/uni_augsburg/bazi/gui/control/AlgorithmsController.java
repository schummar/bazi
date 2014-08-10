package de.uni_augsburg.bazi.gui.control;

import de.uni_augsburg.bazi.gui.view.NumberCheckbox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

public class AlgorithmsController
{
	@FXML NumberCheckbox hareCheck;
	@FXML NumberCheckbox divdwnCheck;
	@FXML NumberCheckbox divstdCheck;
	@FXML NumberCheckbox divgeoCheck;
	@FXML NumberCheckbox divharCheck;
	@FXML NumberCheckbox divupwCheck;
	@FXML NumberCheckbox divpotCheck;
	@FXML NumberCheckbox divstaCheck;
	@FXML NumberCheckbox droopCheck;

	@FXML void initialize()
	{
		ObservableList<NumberCheckbox> selection = FXCollections.observableArrayList();
		hareCheck.setSelection(selection);
		divdwnCheck.setSelection(selection);
		divstdCheck.setSelection(selection);
		divgeoCheck.setSelection(selection);
		divharCheck.setSelection(selection);
		divupwCheck.setSelection(selection);
		divpotCheck.setSelection(selection);
		divstaCheck.setSelection(selection);
		droopCheck.setSelection(selection);
	}
}
