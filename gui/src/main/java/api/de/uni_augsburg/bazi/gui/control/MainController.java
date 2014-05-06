package de.uni_augsburg.bazi.gui.control;

import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;

public class MainController extends VBox
{
	@FXML private TabPane districts;

	public void initialize()
	{
		new TabController(districts, null);
	}
}
