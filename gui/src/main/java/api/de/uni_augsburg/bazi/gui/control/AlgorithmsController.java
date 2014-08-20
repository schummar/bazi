package de.uni_augsburg.bazi.gui.control;

import de.uni_augsburg.bazi.biprop.BipropAlgorithm;
import de.uni_augsburg.bazi.bmm.BMMAlgorithm;
import de.uni_augsburg.bazi.bmm_pow.BMMPowAlgorithm;
import de.uni_augsburg.bazi.common.algorithm.Algorithm;
import de.uni_augsburg.bazi.common.data.BAZIFile;
import de.uni_augsburg.bazi.gui.view.ConfigPopup;
import de.uni_augsburg.bazi.gui.view.NumberCheckbox;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.separate.SeparateAlgorithm;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

public class AlgorithmsController
{
	@FXML ToggleGroup methodTypes;
	@FXML RadioButton simpleRadio;
	@FXML RadioButton minxpRadio;
	@FXML Button minxpButton;
	@FXML RadioButton vpvRadio;
	@FXML RadioButton bmmRadio;
	@FXML Button bmmButton;
	@FXML RadioButton bmmpRadio;
	@FXML Button bmmpButton;
	@FXML RadioButton sepRadio;
	@FXML RadioButton bipropRadio;
	@FXML Button bipropButton;

	@FXML NumberCheckbox hareCheck;
	@FXML NumberCheckbox divdwnCheck;
	@FXML NumberCheckbox divstdCheck;
	@FXML NumberCheckbox divgeoCheck;
	@FXML NumberCheckbox divharCheck;
	@FXML NumberCheckbox divupwCheck;
	@FXML NumberCheckbox divpotCheck;
	@FXML NumberCheckbox divstaCheck;
	@FXML NumberCheckbox droopCheck;

	private ConfigPopup
		minxpPopup, bmmPopup, bmmpPopup, bipropPopup;
	private Property<String>
		minxp = new SimpleStringProperty(),
		bmm_base = new SimpleStringProperty(),
		bmm_min = new SimpleStringProperty(),
		bmm_max = new SimpleStringProperty(),
		biprop = new SimpleStringProperty();

	private BAZIFile data;


	@FXML void initialize()
	{
		ObservableList<NumberCheckbox> selection = FXCollections.observableArrayList();
		hareCheck.setSelection(selection);
		divdwnCheck.setSelection(selection);
		divstdCheck.setSelection(selection);
		divgeoCheck.setSelection(selection);
		divharCheck.setSelection(selection);
		divupwCheck.setSelection(selection);
		divpotCheck.setSelection(selection);
		divstaCheck.setSelection(selection);
		droopCheck.setSelection(selection);

		String minxpText = minxpRadio.getText();
		minxp.addListener(o -> minxpRadio.setText(String.format(minxpText, minxp.getValue())));
		minxp.setValue("10");
		minxpPopup = new ConfigPopup(minxpButton, "/de/uni_augsburg/bazi/gui/config_minxp.fxml");
		((ConfigMinxpController) minxpPopup.contentController()).bind(minxp);

		String bmmText = bmmRadio.getText(), bmmpText = bmmpRadio.getText();
		InvalidationListener bmmListener = o -> {
			bmmRadio.setText(
				String.format(bmmText, bmm_base.getValue(), bmm_min.getValue(), bmm_max.getValue())
			);
			bmmpRadio.setText(
				String.format(bmmpText, bmm_base.getValue(), bmm_min.getValue(), bmm_max.getValue())
			);
		};
		bmm_base.addListener(bmmListener);
		bmm_min.addListener(bmmListener);
		bmm_max.addListener(bmmListener);
		bmm_base.setValue("0");
		bmm_min.setValue("0");
		bmm_max.setValue("oo");
		bmmPopup = new ConfigPopup(bmmButton, "/de/uni_augsburg/bazi/gui/config_bmm.fxml");
		((ConfigBmmController) bmmPopup.contentController()).bind(bmm_base, bmm_min, bmm_max);
		bmmpPopup = new ConfigPopup(bmmpButton, "/de/uni_augsburg/bazi/gui/config_bmm.fxml");
		((ConfigBmmController) bmmpPopup.contentController()).bind(bmm_base, bmm_min, bmm_max);

		bipropPopup = new ConfigPopup(bipropButton, "/de/uni_augsburg/bazi/gui/config_biprop.fxml");


	}

	public void setData(BAZIFile data)
	{
		this.data = data;
		data.algorithmProperty().addListener(this::algorithmChanged);
	}

	@FXML void minxpConfig() { minxpPopup.open(); }
	@FXML void bmmConfig() { bmmPopup.open(); }
	@FXML void bmmpConfig() { bmmpPopup.open(); }
	@FXML void bipropConfig() { bipropPopup.open(); }

	void algorithmChanged(Observable o)
	{
		Algorithm<?> algorithm = data.algorithm().unwrap();
		if (algorithm instanceof BMMAlgorithm)
		{
			bmmRadio.setSelected(true);
			Int base = ((BMMAlgorithm) algorithm).base,
				min = ((BMMAlgorithm) algorithm).min,
				max = ((BMMAlgorithm) algorithm).max;
			bmm_base.setValue(base == null ? "" : base.toString());
			bmm_min.setValue(min == null ? "" : min.toString());
			bmm_max.setValue(max == null ? "" : max.toString());
			selectMethod(((BMMAlgorithm) algorithm).method);
		}
		else if (algorithm instanceof BMMPowAlgorithm)
		{
			bmmpRadio.setSelected(true);
			Int base = ((BMMPowAlgorithm) algorithm).base,
				min = ((BMMPowAlgorithm) algorithm).min,
				max = ((BMMPowAlgorithm) algorithm).max;
			bmm_base.setValue(base == null ? "" : base.toString());
			bmm_min.setValue(min == null ? "" : min.toString());
			bmm_max.setValue(max == null ? "" : max.toString());
			selectMethod(((BMMPowAlgorithm) algorithm).method);
		}
		else if (algorithm instanceof SeparateAlgorithm)
		{
			sepRadio.setSelected(true);
			selectMethod(((SeparateAlgorithm) algorithm).method);
		}
		else if (algorithm instanceof BipropAlgorithm)
		{
			bipropRadio.setSelected(true);
			selectMethod(((BipropAlgorithm) algorithm).Super());
		}
		else
		{
			simpleRadio.setSelected(true);
			selectMethod(algorithm);
		}
	}

	void selectMethod(Algorithm<?> method)
	{
		System.out.println(method);
	}
}
