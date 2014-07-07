package de.uni_augsburg.bazi.gui.control;

import de.schummar.castable.Data;
import de.uni_augsburg.bazi.common.PluginManager;
import de.uni_augsburg.bazi.common.algorithm.Algorithm;
import de.uni_augsburg.bazi.common.algorithm.MatrixData;
import de.uni_augsburg.bazi.common.algorithm.Options;
import de.uni_augsburg.bazi.common.algorithm.VectorData;
import de.uni_augsburg.bazi.common.data.BAZIFile;
import de.uni_augsburg.bazi.common.plain.*;
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

	@FXML private ComboBox<Orientation> orientation;
	@FXML private ComboBox<DivisorFormat> divisorFormat;
	@FXML private ComboBox<TieFormat> tieFormat;
	@FXML private TextArea output;

	private TabController tabController;
	private OutputController outputController;
	public void initialize()
	{
		PluginManager.load();

		tabController = new TabController(districts, addDistrict, data);
		tabController.districtsActivatedProperty()
			.bindBidirectional(districtsActivatedCheckbox.selectedProperty());
		Binding<Boolean> b = EasyBind.map(data.algorithmProperty(), a -> a != null && MatrixData.class.isAssignableFrom(a.dataType()));
		districtsActivatedCheckbox.selectedProperty().bind(b);

		outputController = new OutputController(orientation, divisorFormat, tieFormat, output, data.output().cast(PlainOptions.class));

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

		Platform.runLater(() -> outputController.append(s));

		t = System.currentTimeMillis() - t;
		System.out.println(String.format("%sms", t));
	}
}
