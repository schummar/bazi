package de.uni_augsburg.bazi.gui.control;


import de.uni_augsburg.bazi.common.algorithm.MatrixData;
import de.uni_augsburg.bazi.common.algorithm.VectorData;
import de.uni_augsburg.bazi.common.data.BAZIFile;
import de.uni_augsburg.bazi.common.plain.PlainOptions;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;

import java.util.HashMap;
import java.util.Map;

public class TabController
{
	private final TabPane tabPane;
	private final Button addDistrict;

	private BooleanProperty districtsActivated = new SimpleBooleanProperty(false);
	private final PlainOptions options;
	private final VectorData vData;
	private final MatrixData mData;
	private MatrixData backup = null;

	public TabController(TabPane tabPane, Button addDistrict, BAZIFile data)
	{
		this.tabPane = tabPane;
		this.addDistrict = addDistrict;
		options = data.cast(PlainOptions.class);
		vData = data.cast(VectorData.class);
		mData = data.cast(MatrixData.class);

		districtsActivated.addListener(this::districtsActivatedChanged);
		addDistrict.setOnAction(e -> createNewDistrict());
		addDistrict.visibleProperty().bind(districtsActivatedProperty());
		mData.districts().addListener(this::districtsChanged);

		districtsActivatedChanged(null, true, false);
	}

	private VectorData createNewDistrict()
	{
		VectorData district = mData.districts().addNew();
		district.name("District " + mData.districts().size());
		return district;
	}
	private DistrictTab createNewTab(VectorData district)
	{
		DistrictTab tab = new DistrictTab(options, district);
		tabPane.getTabs().add(tab);
		tabPane.getSelectionModel().select(tabPane.getTabs().size() - 1);
		return tab;
	}
	private Map<VectorData, DistrictTab> districtToTab = new HashMap<>();
	private void districtsChanged(ListChangeListener.Change<? extends VectorData> change)
	{
		while (change.next())
		{
			change.getRemoved().forEach(d -> tabPane.getTabs().remove(districtToTab.get(d)));
			change.getAddedSubList().forEach(d -> districtToTab.put(d, createNewTab(d)));
		}
	}

	private void districtsActivatedChanged(ObservableValue<? extends Boolean> observable, boolean oldValue, boolean newValue)
	{
		if (oldValue == newValue) return;
		if (!newValue)
		{
			if (!mData.districts().isEmpty())
			{
				vData.parties().setAllData(mData.districts().get(0).parties());
				vData.seats(mData.districts().get(0).seats());
			}

			backup = mData.copy().cast(MatrixData.class);
			mData.districts().clear();
			createNewTab(vData);

			tabPane.getStyleClass().add("single");
		}
		else
		{
			tabPane.getTabs().clear();
			if (backup != null)
				mData.districts().setAllData(backup.districts());
			VectorData first = mData.districts().isEmpty()
				? createNewDistrict()
				: mData.districts().get(0);
			first.parties().setAllData(vData.parties());
			first.seats(vData.seats());
			if (first.name() == null)
				first.name("abc");

			vData.parties().clear();
			vData.seats(null);

			tabPane.getStyleClass().remove("single");
		}
	}

	public boolean getDistrictsActivated()
	{
		return districtsActivated.get();
	}
	public BooleanProperty districtsActivatedProperty()
	{
		return districtsActivated;
	}
	public void setDistrictsActivated(boolean districtsActivated)
	{
		this.districtsActivated.set(districtsActivated);
	}
}
