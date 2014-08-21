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

public abstract class ConfigQuotaController
{
	@FXML ComboBox<QuotaFunction> quotaFunctionComboBox;
	@FXML ComboBox<ResidualHandler> residualHandlerComboBox;
	Labeled label;
	public final Property<QuotaAlgorithm> method = new SimpleObjectProperty<>();

	@FXML void initialize()
	{
		quotaFunctionComboBox.setConverter(quotaFunctionStringConverter);
		quotaFunctionComboBox.setItems(quotaFunctions());
		quotaFunctionComboBox.getSelectionModel().select(0);
		quotaFunctionComboBox.getSelectionModel().selectedIndexProperty().addListener((a, b, c) -> update());

		residualHandlerComboBox.setConverter(residualHandlerStringConverter);
		residualHandlerComboBox.setItems(
			FXCollections.observableArrayList(
				ResidualHandler.GREATEST_REMINDERS, ResidualHandler.GREATEST_REMAINDERS_MIN, ResidualHandler.WINNER_TAKES_ALL
			)
		);
		residualHandlerComboBox.getSelectionModel().select(0);
		residualHandlerComboBox.getSelectionModel().selectedIndexProperty().addListener((a, b, c) -> update());

		update();
	}

	abstract ObservableList<QuotaFunction> quotaFunctions();


	public void setLabel(Labeled label)
	{
		this.label = label;
		update();
	}


	private void update()
	{
		QuotaFunction q = quotaFunctionComboBox.getValue();
		ResidualHandler r = residualHandlerComboBox.getValue();
		String name = quotaFunctionStringConverter.toString(q) + residualHandlerStringConverter.toString(r);
		method.setValue(new QuotaAlgorithm(quotaFunctionComboBox.getValue(), residualHandlerComboBox.getValue(), name));
		if (label != null)
		{
			String var = (q == QuotaFunction.HARE || q == QuotaFunction.DROOP) && r == ResidualHandler.GREATEST_REMINDERS
				? "" : " " + Resources.get("gui.methods.var");
			if (q == QuotaFunction.HARE || q == QuotaFunction.HARE_VAR1 || q == QuotaFunction.HARE_VAR2)
				label.setText(Resources.get("gui.methods.hare", name, var));
			else if (q == QuotaFunction.DROOP || q == QuotaFunction.DROOP_VAR1 || q == QuotaFunction.DROOP_VAR2 || q == QuotaFunction.DROOP_VAR3)
				label.setText(Resources.get("gui.methods.droop", name, var));
			else
				throw new RuntimeException();
		}
	}


	private StringConverter<QuotaFunction> quotaFunctionStringConverter = new StringConverter<QuotaFunction>()
	{
		@Override public String toString(QuotaFunction object)
		{
			if (object == QuotaFunction.HARE) return Resources.get("method.hare");
			else if (object == QuotaFunction.HARE_VAR1) return Resources.get("method.hare.var", 1);
			else if (object == QuotaFunction.HARE_VAR2) return Resources.get("method.hare.var", 2);
			else if (object == QuotaFunction.DROOP) return Resources.get("method.droop");
			else if (object == QuotaFunction.DROOP_VAR1) return Resources.get("method.droop.var", 1);
			else if (object == QuotaFunction.DROOP_VAR2) return Resources.get("method.droop.var", 2);
			else if (object == QuotaFunction.DROOP_VAR3) return Resources.get("method.droop.var", 3);
			throw new RuntimeException();
		}
		@Override public QuotaFunction fromString(String string)
		{
			throw new RuntimeException();
		}
	};
	private StringConverter<ResidualHandler> residualHandlerStringConverter = new StringConverter<ResidualHandler>()
	{
		@Override public String toString(ResidualHandler object)
		{
			if (object == ResidualHandler.GREATEST_REMINDERS) return Resources.get("method.residual.grr");
			else if (object == ResidualHandler.GREATEST_REMAINDERS_MIN) return Resources.get("method.residual.gr1");
			else if (object == ResidualHandler.WINNER_TAKES_ALL) return Resources.get("method.residual.wta");
			throw new RuntimeException();
		}
		@Override public ResidualHandler fromString(String string)
		{
			throw new RuntimeException();
		}
	};
}
