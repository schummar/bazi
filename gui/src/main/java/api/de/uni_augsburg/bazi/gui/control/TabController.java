package de.uni_augsburg.bazi.gui.control;

import de.uni_augsburg.bazi.common.data.Data;
import de.uni_augsburg.bazi.gui.mtable.MTable;
import de.uni_augsburg.bazi.gui.view.EditableLabel;
import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Rational;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class TabController
{
	private final TabPane tabPane;
	public TabController(TabPane tabPane, Data data)
	{
		this.tabPane = tabPane;
		tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
		tabPane.getSelectionModel().selectedItemProperty().addListener(this::tabChanged);
		tabPane.getTabs().get(0).setClosable(false);
		createNewTab();
	}

	private void tabChanged(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue)
	{
		if (tabPane.getSelectionModel().getSelectedIndex() == 0)
			Platform.runLater(this::createNewTab);
	}

	private void createNewTab()
	{
		ObservableList<Party> data = FXCollections.observableArrayList(new Party());
		MTable<Party> table = new MTable<>(Party::new);

		table.addColumn(
			new SimpleStringProperty("Name"),
			Party::nameProperty,
			Pos.CENTER_LEFT
		);
		table.addColumn(
			new SimpleStringProperty("Votes"),
			Party::voteProperty,
			x -> x == null ? "" : x.toString(),
			Rational::valueOf,
			Rational::add,
			Pos.CENTER_RIGHT
		);

		table.addColumn(
			new ReadOnlyStringWrapper("Min").getReadOnlyProperty(),
			Party::minProperty,
			x -> x == null ? "" : x.toString(),
			Int::valueOf,
			Int::add,
			Pos.CENTER_RIGHT
		);

		table.addColumn(
			new ReadOnlyStringWrapper("Max").getReadOnlyProperty(),
			Party::maxProperty,
			x -> x == null ? "" : x.toString(),
			Int::valueOf,
			Int::add,
			Pos.CENTER_RIGHT
		);

		table.setItems(data);

		Tab tab = new Tab();
		tab.setGraphic(new EditableLabel(new SimpleStringProperty("District " + tabPane.getTabs().size())));
		tab.setContent(table);
		tabPane.getTabs().add(tab);
		tabPane.getSelectionModel().select(tabPane.getTabs().size() - 1);
	}

	// placeholder
	private class Party
	{
		StringProperty name = new SimpleStringProperty();
		ObjectProperty<Rational> vote = new SimpleObjectProperty<>(BMath.ZERO);
		ObjectProperty<Int> min = new SimpleObjectProperty<>(BMath.ZERO),
			max = new SimpleObjectProperty<>(BMath.INF);

		public StringProperty nameProperty() { return name; }
		public ObjectProperty<Rational> voteProperty() { return vote; }
		public ObjectProperty<Int> minProperty() { return min; }
		public ObjectProperty<Int> maxProperty() { return max; }
	}
}
