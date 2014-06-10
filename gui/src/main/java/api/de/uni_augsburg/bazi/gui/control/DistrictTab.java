package de.uni_augsburg.bazi.gui.control;

import de.schummar.castable.Data;
import de.uni_augsburg.bazi.common.algorithm.VectorData;
import de.uni_augsburg.bazi.common.plain.PlainOptions;
import de.uni_augsburg.bazi.gui.mtable.MTable;
import de.uni_augsburg.bazi.gui.mtable.MTableColumnDefinition;
import de.uni_augsburg.bazi.gui.view.EditableLabel;
import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Real;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;

import java.io.IOException;
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

		partyTable.<Real>addColumn(
			MTableColumnDefinition.create(
				options.voteLabelPropery(),
				VectorData.Party::votesProperty,
				Real::add,
				Real::sub,
				BMath.ZERO,
				Pos.CENTER_RIGHT
			)
		);
		//	partyTable.addColumn(options.nameLabelPropery(), null, Pos.CENTER_LEFT);
		//partyTable.addColumn(options.voteLabelPropery(), null, Pos.CENTER_RIGHT);
	}

/*	private static final MTableColumnDefinition<VectorData.Party, String> NAME_COLUMN = MTableColumnDefinition.create(
		VectorData.Party::nameProperty
	);
	private static final MTableColumnDefinition<VectorData.Party,String> VOTE_COLUMN = MTableColumnDefinition.create(
		VectorData.Party::nameProperty
	);*/
}
