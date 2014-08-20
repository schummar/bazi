package de.uni_augsburg.bazi.gui;

import de.uni_augsburg.bazi.common.Resources;
import de.uni_augsburg.bazi.common.Version;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.LogManager;

public class BAZI extends Application
{
	public static void main(String[] args)
	{
		boolean debug = keyExists(args, "-debug");
		LogManager.getLogManager().getLogger("").setLevel(debug ? Level.ALL : Level.WARNING);
		launch(args);
	}
	private static boolean keyExists(String[] args, String key)
	{
		key = key.toLowerCase();
		for (String s : args)
			if (s.toLowerCase().equals(key))
				return true;
		return false;
	}

	@Override
	public void start(Stage primaryStage) throws IOException
	{
		primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("img/bazi_16.png")));
		primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("img/bazi_32.png")));
		primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("img/bazi_64.png")));
		primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("img/bazi_128.png")));

		primaryStage.setTitle(Resources.get("gui.main_window_title", Version.getCurrentVersionName()));
		Parent content = FXMLLoader.load(
			getClass().getResource("main_window.fxml"),
			ResourceBundle.getBundle("de.uni_augsburg.bazi.common.bazi")
		);
		primaryStage.setMinHeight(600);
		Scene scene = new Scene(content);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
