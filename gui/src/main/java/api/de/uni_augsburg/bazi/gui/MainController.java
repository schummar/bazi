package de.uni_augsburg.bazi.gui;

import de.uni_augsburg.bazi.gui.mtable.MTable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;

public class MainController extends VBox
{
	@FXML private TabPane districts;

	public void initialize()
	{
		ObservableList<Test> data = FXCollections.observableArrayList(new Test());

		MTable<Test> table = new MTable<>(() -> new Test());
		table.addColumn(new SimpleStringProperty("First Name"), Test::firstProperty);
		table.addColumn(new SimpleStringProperty("Last Name"), Test::lastProperty);
		table.setItems(data);

		Tab defaultTab = new Tab("1");
		defaultTab.setContent(table);
		districts.getTabs().add(0, defaultTab);
		districts.getSelectionModel().select(defaultTab);
	}

	private class Test
	{
		public final StringProperty first = new SimpleStringProperty(),
			last = new SimpleStringProperty();

		public StringProperty firstProperty()
		{
			return first;
		}
		public StringProperty lastProperty()
		{
			return last;
		}
	}
}
