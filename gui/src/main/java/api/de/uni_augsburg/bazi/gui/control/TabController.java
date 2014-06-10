package de.uni_augsburg.bazi.gui.control;


import de.schummar.castable.CastableObject;
import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Rational;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class TabController
{
	private final TabPane tabPane;
	public TabController(TabPane tabPane, CastableObject data)
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
		tabPane.getTabs().add(new DistrictTab(tabPane.getTabs().size(), options, district));
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
