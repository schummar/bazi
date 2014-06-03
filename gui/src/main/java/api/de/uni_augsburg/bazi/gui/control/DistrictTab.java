package de.uni_augsburg.bazi.gui.control;

import de.schummar.castable.*;
import de.uni_augsburg.bazi.common.algorithm.VectorData;
import de.uni_augsburg.bazi.gui.mtable.MTable;
import de.uni_augsburg.bazi.gui.mtable.MTableAttribute;
import de.uni_augsburg.bazi.gui.view.EditableLabel;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableObjectValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DistrictTab extends Tab implements Initializable
{
	@FXML private TextField seatsTextField;
	@FXML private MTable<Castable<?>> partyTable;

	private CastableObject data = new CastableObject();

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

	@Override public void initialize(URL location, ResourceBundle resources)
	{
		List<Method> methods = new ArrayList<>();
		for (Method method : VectorData.Party.class.getDeclaredMethods())
			if (method.isAnnotationPresent(de.schummar.castable.Attribute.class))
				methods.add(method);

		ObservableList<Castable<?>> parties = data.get("parties").asCastableList();
		partyTable.setItems(parties);
		if (parties.isEmpty()) parties.add(null);

		partyTable.setSupplier(() -> new CastableObject());
		for (Method method : methods)
			addColumn(method);
	}

	private <T> void addColumn(Method method)
	{
		ObservableObjectValue<String> title;
		/*if (attribute.isLabelEditable())
		{
			DataBinding<String> dataBinding = new DataBinding<>(data, attribute.getName() + "Label");
			if (dataBinding.get() == null) dataBinding.set(attribute.getGuiName());
			title = new ReadOnlyStringWrapper(attribute.getGuiName()).getReadOnlyProperty();
		}
		else*/
		title = new ReadOnlyStringWrapper(method.getName()).getReadOnlyProperty();

		Converter<T> converter = Converters.get(method);
		MTableAttribute<Castable<?>, T> mTableAttribute = MTableAttribute.create(
			c -> c.asCastableObject().getProperty(null),
			t -> converter.applyInverse(t).asCastableString().getValue(),
			s -> converter.apply(new CastableString(s)),
			null,
			null
		);

		Object addOp = null;
		partyTable.addColumn(title, mTableAttribute, addOp == null ? Pos.CENTER_LEFT : Pos.CENTER_RIGHT);
	}
}
