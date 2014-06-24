package de.uni_augsburg.bazi.gui.control;

import de.schummar.castable.Data;
import de.uni_augsburg.bazi.as.ASAlgorithm;
import de.uni_augsburg.bazi.as.DivisorUpdateFunction;
import de.uni_augsburg.bazi.common.algorithm.Algorithm;
import de.uni_augsburg.bazi.common.algorithm.MatrixData;
import de.uni_augsburg.bazi.common.algorithm.Options;
import de.uni_augsburg.bazi.common.data.BAZIFile;
import de.uni_augsburg.bazi.common.plain.PlainFormat;
import de.uni_augsburg.bazi.common.plain.PlainOptions;
import de.uni_augsburg.bazi.divisor.DivisorAlgorithm;
import de.uni_augsburg.bazi.divisor.RoundingFunction;
import de.uni_augsburg.bazi.json.JsonFormat;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.Reader;

public class MainController extends VBox
{
	private final BAZIFile data = Data.create(BAZIFile.class);

	@FXML private TextField title;
	@FXML private Button addDistrict;
	@FXML private TabPane districts;
	@FXML private CheckBox districtsActivatedCheckbox;
	@FXML private Button calculate;
	@FXML private TextArea output;

	public void initialize()
	{
		new TabController(districts, addDistrict, data)
			.districtsActivatedProperty()
			.bindBidirectional(districtsActivatedCheckbox.selectedProperty());

		title.textProperty().bindBidirectional(data.cast(MatrixData.class).nameProperty());
		title.setText("Title....");

		try
		{
			Reader reader = new InputStreamReader(new FileInputStream("C:/Users/Marco/Dropbox/Bazi/testdaten/matrix.json"));
			BAZIFile loaded = new JsonFormat().deserialize(reader).asCastableObject().cast(BAZIFile.class);
			data.src().overwrite(loaded.src());
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}


		calculate.setOnAction(
			e -> {
				Algorithm algorithm = !districtsActivatedCheckbox.isSelected()
					? new DivisorAlgorithm(RoundingFunction.DIV_STD, "DivStd")
					: new ASAlgorithm(
					new DivisorAlgorithm(RoundingFunction.DIV_STD, "DivStd"),
					new DivisorAlgorithm(RoundingFunction.DIV_STD, "DivStd"),
					DivisorUpdateFunction.MIDPOINT
				);

				BAZIFile data = this.data.copy().cast(BAZIFile.class);
				Options options = new Options(20);
				algorithm.apply(data, options);
				PlainFormat format = new PlainFormat(data.output().cast(PlainOptions.class));
				format.configure(algorithm.plainFormatter());
				String s = format.serialize(data.src());
				output.setText(s);
			}
		);
	}
}
