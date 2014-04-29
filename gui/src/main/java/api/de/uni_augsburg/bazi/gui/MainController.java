package de.uni_augsburg.bazi.gui;

import de.uni_augsburg.bazi.gui.mtable.MTable;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
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

		MTable<Test> table = new MTable<>(Test::new);


		table.addColumn(
			new SimpleStringProperty("First Name"),
			Test::firstProperty
		);

		table.addColumn(
			new SimpleStringProperty("Last Name"),
			Test::lastProperty
		);

		table.addColumn(
			new SimpleStringProperty("Age"),
			Test::ageProperty,
			i -> i == null ? "" : i.toString(),
			s -> s == null ? 0 : Integer.parseInt(s),
			(a, b) -> a + b
		);

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
		public final ObservableValue<Integer> age = new SimpleObjectProperty<>(0);

		public Test()
		{
			//first.addListener((a, b, c) -> System.out.println("first: " + c));
			//last.addListener((a, b, c) -> System.out.println("last: " + c));
			//age.addListener((a, b, c) -> System.out.println("age: " + c));
		}

		public String getFirst()
		{
			return first.get();
		}
		public void setFirst(String first)
		{
			this.first.set(first);
		}
		public StringProperty firstProperty()
		{
			return first;
		}
		public String getLast()
		{
			return last.get();
		}
		public void setLast(String last)
		{
			this.last.set(last);
		}
		public StringProperty lastProperty()
		{
			return last;
		}
		public Integer getAge()
		{
			return age.getValue();
		}
		public ObservableValue<Integer> ageProperty()
		{
			return age;
		}
	}
}
