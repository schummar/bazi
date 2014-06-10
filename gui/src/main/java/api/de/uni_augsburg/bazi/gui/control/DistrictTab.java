package de.uni_augsburg.bazi.gui.control;

import de.schummar.castable.*;
import de.uni_augsburg.bazi.common.algorithm.VectorData;
import de.uni_augsburg.bazi.common.plain.PlainOptions;
import de.uni_augsburg.bazi.gui.mtable.MTable;
import de.uni_augsburg.bazi.gui.mtable.MTableAttribute;
import de.uni_augsburg.bazi.gui.view.EditableLabel;
import de.uni_augsburg.bazi.math.Real;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableObjectValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ResourceBundle;

public class DistrictTab extends Tab implements Initializable
{
	@FXML private TextField seatsTextField;
	@FXML private MTable<VectorData.Party> partyTable;

	private final PlainOptions options;
	private final VectorData district;

	public DistrictTab(int number, PlainOptions options, VectorData district)
	{
		this.options = options;
		this.district = district;

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
		if (district.parties().isEmpty()) district.parties().add(null);
		if (options.nameLabel().isEmpty()) options.nameLabel("name");
		if (options.voteLabel().isEmpty()) options.voteLabel("votes");


		partyTable.setItems(district.parties().cast(VectorData.Party.class));
		partyTable.setSupplier(() -> Data.create(VectorData.Party.class));

		partyTable.addColumn(options.nameLabelPropery(), null, Pos.CENTER_LEFT);
		partyTable.addColumn(options.voteLabelPropery(), null, Pos.CENTER_RIGHT);
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

	private static final MTableAttribute<VectorData.Party, String> NAME_COLUMN = MTableAttribute.create(
		VectorData.Party::nameProperty
	);
	private static final MTableAttribute<VectorData.Party,Real> VOTE_COLUMN = MTableAttribute.create(
		VectorData.Party::votesProperty,
	)
}
