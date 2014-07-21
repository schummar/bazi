package de.uni_augsburg.bazi.gui.control;

import de.schummar.castable.Data;
import de.uni_augsburg.bazi.common.PluginManager;
import de.uni_augsburg.bazi.common.algorithm.Algorithm;
import de.uni_augsburg.bazi.common.algorithm.MatrixData;
import de.uni_augsburg.bazi.common.algorithm.Options;
import de.uni_augsburg.bazi.common.algorithm.VectorData;
import de.uni_augsburg.bazi.common.data.BAZIFile;
import de.uni_augsburg.bazi.common.plain.*;
import javafx.application.Platform;
import javafx.beans.binding.Binding;
import javafx.beans.binding.ObjectBinding;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.fxmisc.easybind.EasyBind;

public class MainController
{
	public final BAZIFile data = Data.create(BAZIFile.class);

	@FXML public MenuController menuController;


	@FXML public TextField title;
	@FXML public Button addDistrict;
	@FXML public TabPane districts;
	@FXML public Button calculate;

	@FXML public ComboBox<Orientation> orientation;
	@FXML public ComboBox<DivisorFormat> divisorFormat;
	@FXML public ComboBox<TieFormat> tieFormat;
	@FXML public TextArea output;

	@FXML public Label info;
	private ObjectBinding<String> infoText;

	private TabController tabController;
	private OutputController outputController;

	@FXML public void initialize()
	{
		menuController.main = this;

		PluginManager.load();


		tabController = new TabController(districts, addDistrict, data);
		Binding<Boolean> b = EasyBind.map(data.algorithmProperty(), a -> a != null && MatrixData.class.isAssignableFrom(a.dataType()));

		outputController = new OutputController(orientation, divisorFormat, tieFormat, output, data.output().cast(PlainOptions.class));

		title.textProperty().bindBidirectional(data.cast(VectorData.class).nameProperty());

		calculate.setOnAction(e -> new Thread(this::calc).start());

		infoText = new ObjectBinding<String>()
		{
			{ bind(data.algorithmProperty()); }
			@Override protected String computeValue()
			{
				return "Algorithmus: " + (
					data.algorithm() == null ? "-"
						: data.algorithm().unwrap().name()
				);
			}
		};
		info.textProperty().bind(infoText);
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
