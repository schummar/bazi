package de.uni_augsburg.bazi.gui.control;

import de.uni_augsburg.bazi.common.Resources;
import de.uni_augsburg.bazi.quota.QuotaAlgorithm;
import de.uni_augsburg.bazi.quota.QuotaFunction;
import de.uni_augsburg.bazi.quota.ResidualHandler;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Labeled;
import javafx.util.StringConverter;

public class ConfigHareController extends ConfigQuotaController
{
	@Override ObservableList<QuotaFunction> quotaFunctions()
	{
		return FXCollections.observableArrayList(QuotaFunction.HARE, QuotaFunction.HARE_VAR1, QuotaFunction.HARE_VAR2);
	}
}
