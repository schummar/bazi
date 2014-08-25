package de.uni_augsburg.bazi.gui.control;

import de.uni_augsburg.bazi.quota.QuotaFunction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ConfigDroopController extends ConfigQuotaController
{

	@Override ObservableList<QuotaFunction> quotaFunctions()
	{
		return FXCollections.observableArrayList(QuotaFunction.DROOP, QuotaFunction.DROOP_VAR1, QuotaFunction.DROOP_VAR2, QuotaFunction.DROOP_VAR3);
	}
}
