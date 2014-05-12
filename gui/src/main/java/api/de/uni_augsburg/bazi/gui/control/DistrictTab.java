package de.uni_augsburg.bazi.gui.control;

import de.uni_augsburg.bazi.common.data.Attribute;
import de.uni_augsburg.bazi.common.data.Data;
import de.uni_augsburg.bazi.common.data.MapData;
import de.uni_augsburg.bazi.gui.bind.DataBinding;
import de.uni_augsburg.bazi.gui.mtable.MTable;
import de.uni_augsburg.bazi.gui.mtable.MTableAttribute;
import de.uni_augsburg.bazi.gui.view.EditableLabel;
import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Rational;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableObjectValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class DistrictTab extends Tab implements Initializable
{
	@FXML private TextField seatsTextField;
	@FXML private MTable<Data> partyTable;

	private Data data = Data.create();
	private ObservableList<Data> parties = FXCollections.observableArrayList();
	private StringProperty nameLabel, voteLabel;

	public DistrictTab(int number)
	{
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../district_tab.fxml"));
		fxmlLoader.setController(this);
		fxmlLoader.setResources(ResourceBundle.getBundle("de.uni_augsburg.bazi.common.bazi"));
		try
		{
			setContent(fxmlLoader.load());
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
		setGraphic(new EditableLabel(new SimpleStringProperty("District " + number)));
	}
	public ObservableList<Data> getParties()
	{
		return parties;
	}
	public void setParties(ObservableList<Data> parties)
	{
		this.parties = parties;
	}

	@Override public void initialize(URL location, ResourceBundle resources)
	{
		List<Attribute<?>> attributes = Arrays.asList(
			Attribute.create("name", "Name", true),
			Attribute.create("votes", "Votes", true, Rational::valueOf, Rational::add, BMath.ZERO),
			Attribute.create("min", "Min", false, Int::valueOf, Int::add, BMath.ZERO),
			Attribute.create("max", "Max", false, Int::valueOf, Int::add, BMath.ZERO)
		);

		partyTable.setItems(parties);
		if (parties.isEmpty()) parties.add(Data.create());

		partyTable.setSupplier(Data::create);
		for (Attribute<?> attribute : attributes)
			addColumn(attribute);


	}

	private <T> void addColumn(Attribute<T> attribute)
	{
		ObservableObjectValue<String> title;
		if (attribute.isLabelEditable())
		{
			DataBinding<String> dataBinding = new DataBinding<>(data, attribute.getName() + "Label");
			if (dataBinding.get() == null) dataBinding.set(attribute.getGuiName());
			title = dataBinding;
		}
		else
			title = new ReadOnlyStringWrapper(attribute.getGuiName()).getReadOnlyProperty();

		MTableAttribute<Data, ?> mTableAttribute = MTableAttribute.create(
			d -> new DataBinding<>(d, attribute.getName()),
			attribute.getToString(),
			attribute.getFromString(),
			attribute.getAddOperator(),
			attribute.getNeutralElement()
		);

		partyTable.addColumn(title, mTableAttribute, attribute.getAddOperator() == null ? Pos.CENTER_LEFT : Pos.CENTER_RIGHT);
	}
}
