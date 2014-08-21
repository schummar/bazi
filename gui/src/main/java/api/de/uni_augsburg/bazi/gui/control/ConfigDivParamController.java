package de.uni_augsburg.bazi.gui.control;

import de.uni_augsburg.bazi.common.Resources;
import de.uni_augsburg.bazi.divisor.DivisorAlgorithmPlugin;
import de.uni_augsburg.bazi.divisor.RoundingFunction;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Rational;
import javafx.fxml.FXML;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextField;

import java.util.Map;
import java.util.stream.Collectors;

public abstract class ConfigDivParamController
{
	@FXML TextField paramTextField;
	@FXML TextField exceptionsTextField;
	private Labeled label;

	@FXML void initialize()
	{
		paramTextField.setText(".0");
		paramTextField.textProperty().addListener(o -> update());
		exceptionsTextField.textProperty().addListener(o -> update());
		update();
	}

	public void setLabel(Labeled label)
	{
		this.label = label;
		update();
	}

	private void update()
	{
		if (label != null)
		{
			RoundingFunction.Stationary r = DivisorAlgorithmPlugin.buildStationary(
				paramTextField.getText() + " " + exceptionsTextField.getText()
			);
			label.setText(
				Resources.get(
					getLabelKey(),
					r.getParam(),
					toString(r.getSpecialCases())
				)
			);
		}
	}

	private String toString(Map<Int, Rational> map)
	{
		if (map.isEmpty()) return "";
		return map.entrySet().stream()
			.map(e -> String.format("%s->%s", e.getKey(), e.getValue()))
			.collect(Collectors.joining(", ", "[", "]"));
	}

	abstract String getLabelKey();
}
