package de.uni_augsburg.bazi.gui.control;

import de.schummar.castable.Data;
import de.uni_augsburg.bazi.common.PluginManager;
import de.uni_augsburg.bazi.common.algorithm.Algorithm;
import de.uni_augsburg.bazi.common.algorithm.MatrixData;
import de.uni_augsburg.bazi.common.algorithm.Options;
import de.uni_augsburg.bazi.common.algorithm.VectorData;
import de.uni_augsburg.bazi.common.data.BAZIFile;
import de.uni_augsburg.bazi.common.plain.PlainFormat;
import de.uni_augsburg.bazi.common.plain.PlainOptions;
import de.uni_augsburg.bazi.json.JsonFormat;
import javafx.application.Platform;
import javafx.beans.binding.Binding;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.fxmisc.easybind.EasyBind;

import java.io.File;

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
		PluginManager.load();

		new TabController(districts, addDistrict, data)
			.districtsActivatedProperty()
			.bindBidirectional(districtsActivatedCheckbox.selectedProperty());
		Binding<Boolean> b = EasyBind.map(data.algorithmProperty(), a -> a != null && MatrixData.class.isAssignableFrom(a.dataType()));
		districtsActivatedCheckbox.selectedProperty().bind(b);

		title.textProperty().bindBidirectional(data.cast(VectorData.class).nameProperty());
		//title.setText("Title....");

		File file = new File("input.json");
		JsonFormat json = new JsonFormat();
		BAZIFile loaded = json.deserialize(file).asCastableObject().cast(BAZIFile.class);
		data.src().overwrite(loaded.src());

		calculate.setOnAction(e -> new Thread(this::calc).start());
	}

	private void calc()
	{

		long t = System.currentTimeMillis();

		BAZIFile data = this.data.copy().cast(BAZIFile.class);
		Algorithm<?> algorithm = data.algorithm();


		Options options = new Options(20);
		algorithm.apply(data, options);
		PlainFormat format = new PlainFormat(data.output().cast(PlainOptions.class));
		format.configure(algorithm.plainFormatter());
		String s = format.serialize(data.src());

		Platform.runLater(() -> output.setText(s));

		t = System.currentTimeMillis() - t;
		System.out.println(String.format("%sms", t));
	}
}
