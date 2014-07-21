package de.uni_augsburg.bazi.gui.control;

import de.schummar.castable.Data;
import de.uni_augsburg.bazi.common.algorithm.VectorData;
import de.uni_augsburg.bazi.common.plain.PlainOptions;
import de.uni_augsburg.bazi.gui.bind.Constant;
import de.uni_augsburg.bazi.gui.mtable.MTable;
import de.uni_augsburg.bazi.gui.mtable.MTableColumnDefinition;
import de.uni_augsburg.bazi.gui.view.EditableLabel;
import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Real;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static de.uni_augsburg.bazi.common.algorithm.VectorData.Party;

public class DistrictTab extends Tab implements Initializable
{
	@FXML private TextField seatsTextField;
	@FXML private MTable<Party> partyTable;

	private final PlainOptions options;
	private VectorData district;

	public DistrictTab(PlainOptions options, VectorData district)
	{
		this.options = options;
		this.district = district;

		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/de/uni_augsburg/bazi/gui/district_tab.fxml"));
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
		setGraphic(new EditableLabel(district.nameProperty()));
	}

	public VectorData getDistrict()
	{
		return district;
	}
	public void setDistrict(VectorData district)
	{
		this.district = district;
		partyTable.setItems(district.parties().cast(Party.class));
	}

	@Override public void initialize(URL location, ResourceBundle resources)
	{
		if (options.nameLabel() == null) options.nameLabel("name");
		if (options.voteLabel() == null) options.voteLabel("votes");
		seatsTextField.textProperty().bindBidirectional(district.seatsProperty().asStringProperty());


		partyTable.setItems(district.parties().cast(Party.class));
		partyTable.setSupplier(() -> Data.create(Party.class));

		partyTable.addColumn(
			MTableColumnDefinition.create(
				options.nameLabelPropery(),
				Party::nameProperty,
				Pos.CENTER_LEFT
			)
		);

		partyTable.addColumn(
			MTableColumnDefinition.create(
				options.voteLabelPropery(),
				party -> party.votesProperty().asStringProperty(),
				Party::votesProperty,
				Real::add,
				Real::sub,
				BMath.ZERO,
				Pos.CENTER_RIGHT
			)
		);

		partyTable.addColumn(
			MTableColumnDefinition.create(
				Constant.of("min"),
				party -> party.minProperty().asStringProperty(),
				Party::minProperty,
				Int::add,
				Int::sub,
				BMath.ZERO,
				Pos.CENTER_RIGHT
			)
		);

		partyTable.addColumn(
			MTableColumnDefinition.create(
				Constant.of("max"),
				party -> party.maxProperty().asStringProperty(),
				Party::maxProperty,
				Int::add,
				Int::sub,
				BMath.INF,
				Pos.CENTER_RIGHT
			)
		);

		if (partyTable.getItems().size() == 0) partyTable.newRow(0);
	}
}
