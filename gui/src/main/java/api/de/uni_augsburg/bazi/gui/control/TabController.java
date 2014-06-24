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

	private DistrictTab singleTab = null;
	private Map<VectorData, DistrictTab> districts = new HashMap<>();

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
	private void districtsChanged(ListChangeListener.Change<? extends VectorData> change)
	{
		while (change.next())
		{
			change.getRemoved().forEach(d -> tabPane.getTabs().remove(districts.get(d)));
			change.getAddedSubList().forEach(d -> districts.put(d, createNewTab(d)));
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

			mData.districts().clear();
			singleTab = createNewTab(vData);

			tabPane.getStyleClass().add("single");
		}
		else
		{
			tabPane.getTabs().remove(singleTab);
			if (mData.districts().isEmpty())
			{
				VectorData first = createNewDistrict();
				first.parties().setAllData(vData.parties());
				first.seats(vData.seats());
				first.name(vData.name() == null ? "abc" : vData.name());
			}

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
