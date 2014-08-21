package de.uni_augsburg.bazi.gui.control;

import de.uni_augsburg.bazi.biprop.BipropAlgorithm;
import de.uni_augsburg.bazi.bmm.BMMAlgorithm;
import de.uni_augsburg.bazi.bmm_pow.BMMPowAlgorithm;
import de.uni_augsburg.bazi.common.algorithm.Algorithm;
import de.uni_augsburg.bazi.common.data.BAZIFile;
import de.uni_augsburg.bazi.divisor.DivisorAlgorithm;
import de.uni_augsburg.bazi.divisor.RoundingFunction;
import de.uni_augsburg.bazi.gui.view.ConfigPopup;
import de.uni_augsburg.bazi.gui.view.NumberCheckbox;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.quota.QuotaAlgorithm;
import de.uni_augsburg.bazi.quota.QuotaFunction;
import de.uni_augsburg.bazi.separate.SeparateAlgorithm;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

public class AlgorithmsController
{
	@FXML ToggleGroup methodTypes;
	@FXML RadioButton simpleRadio;
	@FXML RadioButton minxpRadio;
	@FXML Button minxpButton;
	@FXML Label minxpAux;
	@FXML RadioButton vpvRadio;
	@FXML RadioButton bmmRadio;
	@FXML Button bmmButton;
	@FXML Label bmmAux;
	@FXML RadioButton bmmpRadio;
	@FXML Button bmmpButton;
	@FXML Label bmmpAux;
	@FXML RadioButton sepRadio;
	@FXML RadioButton bipropRadio;
	@FXML Button bipropButton;

	private final ObservableList<NumberCheckbox> selection = FXCollections.observableArrayList();
	@FXML NumberCheckbox hareCheck;
	@FXML Button hareButton;
	@FXML NumberCheckbox divdwnCheck;
	@FXML NumberCheckbox divstdCheck;
	@FXML NumberCheckbox divgeoCheck;
	@FXML NumberCheckbox divharCheck;
	@FXML NumberCheckbox divupwCheck;
	@FXML NumberCheckbox divpotCheck;
	@FXML Button divpotButton;
	@FXML Label divpotAux;
	@FXML NumberCheckbox divstaCheck;
	@FXML Button divstaButton;
	@FXML Label divstaAux;
	@FXML NumberCheckbox droopCheck;
	@FXML Button droopButton;

	private ConfigPopup
		minxpPopup, bmmPopup, bmmpPopup, bipropPopup, harePopup, divpotPopup, divstaPopup, droopPopup;

	private BAZIFile data;


	@FXML void initialize()
	{
		hareCheck.setSelection(selection);
		divdwnCheck.setSelection(selection);
		divstdCheck.setSelection(selection);
		divgeoCheck.setSelection(selection);
		divharCheck.setSelection(selection);
		divupwCheck.setSelection(selection);
		divpotCheck.setSelection(selection);
		divstaCheck.setSelection(selection);
		droopCheck.setSelection(selection);


		minxpPopup = new ConfigPopup(minxpButton, "/de/uni_augsburg/bazi/gui/config_minxp.fxml");
		((ConfigMinxpController) minxpPopup.contentController()).setLabel(minxpAux);

		bmmPopup = new ConfigPopup(bmmButton, "/de/uni_augsburg/bazi/gui/config_bmm.fxml");
		((ConfigBmmController) bmmPopup.contentController()).setLabel(bmmAux);

		bmmpPopup = new ConfigPopup(bmmpButton, "/de/uni_augsburg/bazi/gui/config_bmm.fxml");
		((ConfigBmmController) bmmpPopup.contentController()).setLabel(bmmpAux);

		bipropPopup = new ConfigPopup(bipropButton, "/de/uni_augsburg/bazi/gui/config_biprop.fxml");

		harePopup = new ConfigPopup(hareButton, "/de/uni_augsburg/bazi/gui/config_hare.fxml");
		((ConfigHareController) harePopup.contentController()).setLabel(hareCheck);

		divpotPopup = new ConfigPopup(divpotButton, "/de/uni_augsburg/bazi/gui/config_divpot.fxml");
		((ConfigDivpotController) divpotPopup.contentController()).setLabel(divpotAux);

		divstaPopup = new ConfigPopup(divstaButton, "/de/uni_augsburg/bazi/gui/config_divsta.fxml");
		((ConfigDivstaController) divstaPopup.contentController()).setLabel(divstaAux);

		droopPopup = new ConfigPopup(droopButton, "/de/uni_augsburg/bazi/gui/config_droop.fxml");
		((ConfigDroopController) droopPopup.contentController()).setLabel(droopCheck);
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
	@FXML void hareConfig() { harePopup.open(); }
	@FXML void divpotConfig() { divpotPopup.open(); }
	@FXML void divstaConfig() { divstaPopup.open(); }
	@FXML void droopConfig() { droopPopup.open(); }

	void algorithmChanged(Observable o)
	{
		Algorithm<?> algorithm = data.algorithm().unwrap();
		if (algorithm instanceof BMMAlgorithm)
		{
			bmmRadio.setSelected(true);
			Int base = ((BMMAlgorithm) algorithm).base,
				min = ((BMMAlgorithm) algorithm).min,
				max = ((BMMAlgorithm) algorithm).max;
			selectMethod(((BMMAlgorithm) algorithm).method);
		}
		else if (algorithm instanceof BMMPowAlgorithm)
		{
			bmmpRadio.setSelected(true);
			Int base = ((BMMPowAlgorithm) algorithm).base,
				min = ((BMMPowAlgorithm) algorithm).min,
				max = ((BMMPowAlgorithm) algorithm).max;
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
		selection.clear();
		if (method instanceof QuotaAlgorithm)
		{
			QuotaFunction q = ((QuotaAlgorithm) method).quotaFunction;
			if (q == QuotaFunction.HARE
				|| q == QuotaFunction.HARE_VAR1
				|| q == QuotaFunction.HARE_VAR2)
			{
				hareCheck.setSelected(true);
			}
			else if (q == QuotaFunction.DROOP
				|| q == QuotaFunction.DROOP_VAR1
				|| q == QuotaFunction.DROOP_VAR2
				|| q == QuotaFunction.DROOP_VAR3)
			{
				droopCheck.setSelected(true);
			}
		}
		else if (method instanceof DivisorAlgorithm)
		{
			RoundingFunction r = ((DivisorAlgorithm) method).roundingFunction;
			if (r == RoundingFunction.DIV_STD) divstdCheck.setSelected(true);
			else if (r == RoundingFunction.DIV_DWN) divdwnCheck.setSelected(true);
			else if (r == RoundingFunction.DIV_UPW) divupwCheck.setSelected(true);
			else if (r == RoundingFunction.DIV_GEO) divgeoCheck.setSelected(true);
			else if (r == RoundingFunction.DIV_HAR) hareCheck.setSelected(true);
			else if (r instanceof RoundingFunction.Stationary) divstaCheck.setSelected(true);
			else if (r instanceof RoundingFunction.Power) divpotCheck.setSelected(true);
		}
	}
}
